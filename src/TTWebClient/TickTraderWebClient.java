package TTWebClient;

import TTWebClient.Domain.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TickTraderWebClient implements AutoCloseable {

    private String _webApiAddress;
    private String _webApiId;
    private String _webApiKey;
    private String _webApiSecret;
    private CloseableHttpAsyncClient _httpClient;
    ExecutorService executor = Executors.newCachedThreadPool();
    private Gson _gson;

    HttpAsyncClientBuilder _builder = HttpAsyncClientBuilder.create();

    //private MediaTypeFormatterCollection _formatters;


    /// <summary>
    /// Construct public TickTrader Web API client
    /// </summary>
    /// <remarks>Public Web API client will access only to public methods that don't require authentication!</remarks>
    /// <param name="webApiAddress">Web API address</param>
    public TickTraderWebClient(String webApiAddress) {
        if (webApiAddress == null)
            throw new IllegalArgumentException("webApiAddress");

        _webApiAddress = webApiAddress;
        ArrayList<Header> headerCollection = new ArrayList<>();
        headerCollection.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate"));
        headerCollection.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        _builder.setDefaultHeaders(headerCollection);
        //_formatters = new MediaTypeFormatterCollection();
        //_formatters.Clear();
        //_formatters.Add(new JilMediaTypeFormatter());
    }

    /// <summary>
    /// Construct TickTrader Web API client
    /// </summary>
    /// <param name="webApiAddress">Web API address</param>
    /// <param name="webApiId">Web API token Id</param>
    /// <param name="webApiKey">Web API token key</param>
    /// <param name="webApiSecret">Web API token secret</param>
    public TickTraderWebClient(String webApiAddress, String webApiId, String webApiKey, String webApiSecret) {
        this(webApiAddress);
        _webApiId = webApiId;
        _webApiKey = webApiKey;
        _webApiSecret = webApiSecret;

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(date.getTime());
            }
        });
        _gson = builder.create();
    }

    @Override
    public void close() throws Exception {
        _httpClient.close();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /// <summary>
    /// Force to ignore server SSL/TLS ceritficate
    /// </summary>
    public void IgnoreServerCertificate() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null,
                new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {

                        return null;
                    }

                    public void checkClientTrusted(
                            X509Certificate[] certs, String authType) {

                    }

                    public void checkServerTrusted(
                            X509Certificate[] certs, String authType) {

                    }
                }}, new SecureRandom());

        _builder.setSSLContext(sslContext);
        _builder.setSSLHostnameVerifier(new NoopHostnameVerifier());
    }

    private HttpRequestBase GetHMACRequest(HttpRequestBase req) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String message = "Account Web API methods requeies valid Web API token (Id, Key, Secret)!";
        if (_webApiId == null || _webApiId == "")
            throw new IllegalArgumentException(message + " illegal webApiId");
        if (_webApiKey == null || _webApiKey == "")
            throw new IllegalArgumentException(message + " illegal webApiKey");
        if (_webApiSecret == null || _webApiSecret == "")
            throw new IllegalArgumentException(message + " illegal webApiSecret");

        long timestamp = new Date(System.currentTimeMillis()).getTime();
        String content = "";

        if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(req.getClass())) {
            HttpEntityEnclosingRequestBase reqWithContent = (HttpEntityEnclosingRequestBase) req;
            if (reqWithContent != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(reqWithContent.getEntity().getContent()));
                StringBuffer resultStr = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    resultStr.append(line);
                }
                content = resultStr.toString();
            }
        }
        String signature = timestamp + _webApiId + _webApiKey + req.getMethod() + req.getURI() + content;
        byte[] messageBytes = signature.getBytes(StandardCharsets.US_ASCII);
        byte[] hmacKeyByte = _webApiSecret.getBytes(StandardCharsets.US_ASCII);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(hmacKeyByte, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] mac_data = sha256_HMAC.doFinal(messageBytes);

        byte[] valueDecoded = Base64.encodeBase64(mac_data);
        String base64HMACStr = new String(valueDecoded);
        req.setHeader(HttpHeaders.AUTHORIZATION, String.format("%1$s %2$s:%3$s:%4$s:%5$s", "HMAC ", _webApiId, _webApiKey, timestamp, base64HMACStr));
        return req;
    }

    private CloseableHttpAsyncClient CreatePublicHttpClient() throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (_httpClient == null) {
            _httpClient = _builder.build();
            _httpClient.start();
        }
        return _httpClient;
    }


    private class CreatePublicHttpClientWrapper implements Callable<CloseableHttpAsyncClient> {
        public CloseableHttpAsyncClient call() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
            return CreatePublicHttpClient();
        }
    }

    private CloseableHttpAsyncClient CreatePrivateHttpClient() {
        if (_httpClient == null) {
            _httpClient = _builder.build();
            _httpClient.start();
        }
        return _httpClient;
    }

    private class CreatePrivateHttpClientWrapper implements Callable<CloseableHttpAsyncClient> {
        public CloseableHttpAsyncClient call() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
            return CreatePrivateHttpClient();
        }
    }

    private Future<HttpResponse> PublicHttpGetAsyncStart(String method) {
        HttpGet getReq = new HttpGet(_webApiAddress + "/" + method);
        return HttpExecuteRequestAsync(new CreatePublicHttpClientWrapper(), getReq);
    }

    private Future<HttpResponse> PrivateHttpGetAsyncStart(String method) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpGet getReq = new HttpGet(_webApiAddress + "/" + method);
        HttpGet hmacReq = (HttpGet) GetHMACRequest(getReq);
        return HttpExecuteRequestAsync(new CreatePrivateHttpClientWrapper(), hmacReq);
    }

    private <TRequest> Future<HttpResponse> PrivateHttpPostAsyncStart(String method, TRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpPost postReq = new HttpPost(_webApiAddress + "/" + method);

        String requestJson = _gson.toJson(request);
        postReq.setEntity(new StringEntity(requestJson, ContentType.APPLICATION_JSON));
        HttpPost hmacReq = (HttpPost) GetHMACRequest(postReq);
        return HttpExecuteRequestAsync(new CreatePrivateHttpClientWrapper(), hmacReq);
    }

    private <TRequest> Future<HttpResponse> PrivateHttpPutAsyncStart(String method, TRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpPut putReq = new HttpPut(_webApiAddress + "/" + method);

        String requestJson = _gson.toJson(request);
        putReq.setEntity(new StringEntity(requestJson, ContentType.APPLICATION_JSON));
        HttpPut hmacReq = (HttpPut) GetHMACRequest(putReq);
        return HttpExecuteRequestAsync(new CreatePrivateHttpClientWrapper(), hmacReq);
    }

    private Future<HttpResponse> PrivateHttpDeleteAsyncStart(String method) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpDelete postReq = new HttpDelete(_webApiAddress + "/" + method);
        HttpDelete hmacReq = (HttpDelete) GetHMACRequest(postReq);
        return HttpExecuteRequestAsync(new CreatePrivateHttpClientWrapper(), hmacReq);
    }

    private <TResult> TResult HttpRequestEnd(Type type, Future<HttpResponse> responseFuture) throws IOException, ExecutionException, InterruptedException {
        HttpResponse response = responseFuture.get();
        BufferedReader rd = new BufferedReader(new InputStreamReader(new GZIPInputStream(response.getEntity().getContent())));
        StringBuffer resultStr = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            resultStr.append(line);
        }

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), resultStr.toString());

        TResult result = _gson.fromJson(String.valueOf(resultStr), type);
        return result;
    }

    private Future<HttpResponse> HttpExecuteRequestAsync(Callable<CloseableHttpAsyncClient> clientFactory, HttpRequestBase request) {
        try {
            CloseableHttpAsyncClient client = clientFactory.call();
            Future<HttpResponse> futureResponse = client.execute(request, null);
            return futureResponse;
        } catch (Exception ex) {
            return null;
        }
    }

    /// <summary>
    /// Get public trade session information
    /// </summary>
    /// <returns>Public trade session information</returns>
    public Future<HttpResponse> GetPublicTradeSessionAsyncStart() {
        return PublicHttpGetAsyncStart("api/v1/public/tradesession");
    }

    public TTTradeSession GetPublicTradeSessionAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTradeSession.class, httpResponseFuture);
    }

    public TTTradeSession GetPublicTradeSession() throws Exception {
        return GetPublicTradeSessionAsyncEnd(GetPublicTradeSessionAsyncStart());
    }


    /// <summary>
    /// Get list of all available public currencies
    /// </summary>
    /// <returns>List of all available public currencies</returns>
    public Future<HttpResponse> GetPublicAllCurrenciesAsyncStart() {
        return PublicHttpGetAsyncStart("api/v1/public/currency");
    }

    public ArrayList<TTCurrency> GetPublicAllCurrenciesAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTCurrency>>() {
        }.getType(), httpResponseFuture);
    }


    public ArrayList<TTCurrency> GetPublicAllCurrencies() throws InterruptedException, ExecutionException, IOException {
        return GetPublicAllCurrenciesAsyncEnd(GetPublicAllCurrenciesAsyncStart());
    }


    /// <summary>
    /// Get filtered list of public currencies
    /// </summary>
    /// <param name="filter">Currency names separated by space character</param>
    /// <returns>List of filtered public currencies</returns>
    public Future<HttpResponse> GetPublicCurrencyAsyncStart(String filter) throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart(String.format("api/v1/public/currency/%1$s", URLEncoder.encode(filter, "UTF-8")));
    }

    public ArrayList<TTCurrency> GetPublicCurrencyAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTCurrency>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTCurrency> GetPublicCurrency(String filter) throws InterruptedException, ExecutionException, IOException {
        return GetPublicCurrencyAsyncEnd(GetPublicCurrencyAsyncStart(filter));
    }


    /// <summary>
    /// Get list of all available public symbols
    /// </summary>
    /// <returns>List of all available public symbols</returns>
    public Future<HttpResponse> GetPublicAllSymbolsAsyncStart() throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart("api/v1/public/symbol");
    }

    public ArrayList<TTSymbol> GetPublicAllSymbolsAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTSymbol>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTSymbol> GetPublicAllSymbols() throws InterruptedException, ExecutionException, IOException {
        return GetPublicAllSymbolsAsyncEnd(GetPublicAllSymbolsAsyncStart());
    }


    /// <summary>
    /// Get filtered list of public symbols
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public symbols</returns>
    public Future<HttpResponse> GetPublicSymbolAsyncStart(String filter) throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart(String.format("api/v1/public/symbol/%1$s", URLEncoder.encode(filter, "UTF-8")));
    }

    public ArrayList<TTSymbol> GetPublicSymbolAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTSymbol>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTSymbol> GetPublicSymbol(String filter) throws InterruptedException, ExecutionException, IOException {
        return GetPublicSymbolAsyncEnd(GetPublicSymbolAsyncStart(filter));
    }

    /// <summary>
    /// Get list of all available public feed ticks
    /// </summary>
    /// <returns>List of all available public feed ticks</returns>
    public Future<HttpResponse> GetPublicAllTicksAsyncStart() throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart("api/v1/public/tick");
    }

    public ArrayList<TTFeedTick> GetPublicAllTicksAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTick>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTick> GetPublicAllTicks() throws InterruptedException, ExecutionException, IOException {
        return GetPublicAllTicksAsyncEnd(GetPublicAllTicksAsyncStart());
    }

    /// <summary>
    /// Get filtered list of public feed ticks
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public feed ticks</returns>
    public Future<HttpResponse> GetPublicTickAsyncStart(String symbol) throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart(String.format("api/v1/public/tick/%1$s", URLEncoder.encode(symbol, "UTF-8")));
    }

    public ArrayList<TTFeedTick> GetPublicTickAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTick>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTick> GetPublicTick(String symbol) throws InterruptedException, ExecutionException, IOException {
        return GetPublicTickAsyncEnd(GetPublicTickAsyncStart(symbol));
    }


    /// <summary>
    /// Get list of all available public feed level2 ticks
    /// </summary>
    /// <returns>List of all available public feed level2 ticks</returns>
    public Future<HttpResponse> GetPublicAllTicksLevel2AsyncStart() throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart("api/v1/public/level2");
    }

    public ArrayList<TTFeedTickLevel2> GetPublicAllTicksLevel2AsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTickLevel2>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTickLevel2> GetPublicAllTicksLevel2() throws InterruptedException, ExecutionException, IOException {
        return GetPublicAllTicksLevel2AsyncEnd(GetPublicAllTicksLevel2AsyncStart());
    }

    /// <summary>
    /// Get filtered list of public feed level2 ticks
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public feed level2 ticks</returns>
    public Future<HttpResponse> GetPublicTickLevel2AsyncStart(String symbol) throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart(String.format("api/v1/public/level2/%1$s", URLEncoder.encode(symbol, "UTF-8")));
    }

    public ArrayList<TTFeedTickLevel2> GetPublicTickLevel2AsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTickLevel2>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTickLevel2> GetPublicTickLevel2(String symbol) throws InterruptedException, ExecutionException, IOException {
        return GetPublicTickLevel2AsyncEnd(GetPublicTickLevel2AsyncStart(symbol));
    }


    /// <summary>
    /// Get list of all available public symbol statistics
    /// </summary>
    /// <returns>List of all available public symbol statistics</returns>
    public Future<HttpResponse> GetPublicAllTickersAsyncStart() throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart("api/v1/public/ticker");
    }

    public ArrayList<TTTicker> GetPublicAllTickersAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTTicker>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTTicker> GetPublicAllTickers() throws InterruptedException, ExecutionException, IOException {
        return GetPublicAllTickersAsyncEnd(GetPublicAllTickersAsyncStart());
    }


    /// <summary>
    /// Get filtered list of public symbol statistics
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public symbol statistics</returns>
    public Future<HttpResponse> GetPublicTickerAsyncStart(String filter) throws UnsupportedEncodingException {
        return PublicHttpGetAsyncStart(String.format("api/v1/public/ticker/%1$s", URLEncoder.encode(filter, "UTF-8")));
    }

    public ArrayList<TTTicker> GetPublicTickerAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTTicker>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTTicker> GetPublicTicker(String filter) throws InterruptedException, ExecutionException, IOException {
        return GetPublicTickerAsyncEnd(GetPublicTickerAsyncStart(filter));
    }


    /// <summary>
    /// Get account information
    /// </summary>
    /// <returns>Account information</returns>
    public Future<HttpResponse> GetAccountAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart("api/v1/account");
    }

    public TTAccount GetAccountAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTAccount.class, httpResponseFuture);
    }

    public TTAccount GetAccount() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAccountAsyncEnd(GetAccountAsyncStart());
    }

    /// <summary>
    /// Get trade session information
    /// </summary>
    /// <returns>Trade session information</returns>
    public Future<HttpResponse> GetTradeSessionAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart("api/v1/tradesession");
    }

    public TTTradeSession GetTradeSessionAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTradeSession.class, httpResponseFuture);
    }

    public TTTradeSession GetTradeSession() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetTradeSessionAsyncEnd(GetTradeSessionAsyncStart());
    }


    /// <summary>
    /// Get list of all available currencies
    /// </summary>
    /// <returns>List of all available currencies</returns>
    public Future<HttpResponse> GetAllCurrenciesAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart("api/v1/currency");
    }

    public ArrayList<TTCurrency> GetAllCurrenciesAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTCurrency>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTCurrency> GetAllCurrencies() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllCurrenciesAsyncEnd(GetAllCurrenciesAsyncStart());
    }

    /// <summary>
    /// Get filtered list of currencies
    /// </summary>
    /// <param name="filter">Currency names separated by space character</param>
    /// <returns>List of filtered public currencies</returns>
    public Future<HttpResponse> GetCurrencyAsyncStart(String filter) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/currency/%1$s", filter));
    }

    public ArrayList<TTCurrency> GetCurrencyAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTCurrency>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTCurrency> GetCurrency(String filter) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetCurrencyAsyncEnd(GetCurrencyAsyncStart(filter));
    }

    /// <summary>
    /// Get list of all available symbols
    /// </summary>
    /// <returns>List of all available symbols</returns>
    public Future<HttpResponse> GetAllSymbolsAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart("api/v1/symbol");
    }

    public ArrayList<TTSymbol> GetAllSymbolsAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTSymbol>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTSymbol> GetAllSymbols() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllSymbolsAsyncEnd(GetAllSymbolsAsyncStart());
    }

    /// <summary>
    /// Get filtered list of symbols
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public symbols</returns>
    public Future<HttpResponse> GetSymbolAsyncStart(String filter) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/symbol/%1$s", filter));
    }

    public ArrayList<TTSymbol> GetSymbolAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTSymbol>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTSymbol> GetSymbol(String filter) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetSymbolAsyncEnd(GetSymbolAsyncStart(filter));
    }

    /// <summary>
    /// Get list of all available feed tick
    /// </summary>
    /// <returns>List of all available feed tick</returns>
    public Future<HttpResponse> GetAllTicksAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/tick"));
    }

    public ArrayList<TTFeedTick> GetAllTicksAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTick>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTick> GetAllTicks() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllTicksAsyncEnd(GetAllTicksAsyncStart());
    }

    /// <summary>
    /// Get filtered list of public feed ticks
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public feed ticks</returns>
    public Future<HttpResponse> GetTickAsyncStart(String symbol) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/tick/%1$s", symbol));
    }

    public ArrayList<TTFeedTick> GetTickAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTick>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTick> GetTick(String symbol) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetPublicTickAsyncEnd(GetPublicTickAsyncStart(symbol));
    }

    /// <summary>
    /// Get list of all available feed level2 tick
    /// </summary>
    /// <returns>List of all available feed level2 tick</returns>
    public Future<HttpResponse> GetAllTicksLevel2AsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/level2"));
    }

    public ArrayList<TTFeedTickLevel2> GetAllTicksLevel2AsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTickLevel2>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTickLevel2> GetAllTicksLevel2() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllTicksLevel2AsyncEnd(GetAllTicksLevel2AsyncStart());
    }


    /// <summary>
    /// Get filtered list of feed level2 ticks
    /// </summary>
    /// <param name="filter">Symbols names separated by space character</param>
    /// <returns>List of filtered public feed level2 ticks</returns>
    public Future<HttpResponse> GetTickLevel2AsyncStart(String symbol) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/level2/%1$s", symbol));
    }

    public ArrayList<TTFeedTickLevel2> GetTickLevel2AsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTFeedTickLevel2>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTFeedTickLevel2> GetTickLevel2(String symbol) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetTickLevel2AsyncEnd(GetTickLevel2AsyncStart(symbol));
    }


    /// <summary>
    /// Get list of all cash account assets (currency with amount)
    /// </summary>
    /// <remarks>
    /// **Works only for cash accounts!**
    /// </remarks>>
    /// <returns>List of all cash account assets</returns>
    public Future<HttpResponse> GetAllAssetsAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/asset"));
    }

    public ArrayList<TTAsset> GetAllAssetsAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTAsset>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTAsset> GetAllAssets() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllAssetsAsyncEnd(GetAllAssetsAsyncStart());
    }


    /// <summary>
    /// Get cash account asset (currency with amount) by the given currency name
    /// </summary>
    /// <remarks>
    /// **Works only for cash accounts!**
    /// </remarks>>
    /// <param name="currency">Currency name</param>
    /// <returns>Cash account asset for the given currency</returns>
    public Future<HttpResponse> GetAssetAsyncStart(String currency) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/level2/%1$s", currency));
    }

    public TTAsset GetAssetAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTAsset.class, httpResponseFuture);
    }

    public TTAsset GetAsset(String currency) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAssetAsyncEnd(GetAssetAsyncStart(currency));
    }


    /// <summary>
    /// Get list of all available positions
    /// </summary>
    /// <remarks>
    /// **Works only for net accounts!**
    /// </remarks>>
    /// <returns>List of all available positions</returns>
    public Future<HttpResponse> GetAllPositionsAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/position"));
    }

    public ArrayList<TTPosition> GetAllPositionsAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTPosition>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTPosition> GetAllPositions() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllPositionsAsyncEnd(GetAllPositionsAsyncStart());
    }

    /// <summary>
    /// Get position by Id or symbol name
    /// </summary>
    /// <remarks>
    /// **Works only for net accounts!**
    /// </remarks>>
    /// <param name="id">Position Id or symbol name</param>
    /// <returns>Position</returns>
    public Future<HttpResponse> GetPositionAsyncStart(String id) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/position/%1$s", id));
    }

    public TTPosition GetPositionAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTPosition.class, httpResponseFuture);
    }

    public TTPosition GetPosition(String id) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetPositionAsyncEnd(GetPositionAsyncStart(id));
    }


    /// <summary>
    /// Get list of all available trades
    /// </summary>
    /// <returns>List of all available trades</returns>
    public Future<HttpResponse> GetAllTradesAsyncStart() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/trade"));
    }

    public ArrayList<TTTrade> GetAllTradesAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(new TypeToken<List<TTTrade>>() {
        }.getType(), httpResponseFuture);
    }

    public ArrayList<TTTrade> GetAllTrades() throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetAllTradesAsyncEnd(GetAllTradesAsyncStart());
    }


    /// <summary>
    /// Get trade by Id
    /// </summary>
    /// <param name="tradeId">Trade Id</param>
    /// <returns>Trade</returns>
    public Future<HttpResponse> GetTradeAsyncStart(long tradeId) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpGetAsyncStart(String.format("api/v1/trade/%1$s", tradeId));
    }

    public TTTrade GetTradeAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTrade.class, httpResponseFuture);
    }

    public TTTrade GetTrade(long tradeId) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetTradeAsyncEnd(GetTradeAsyncStart(tradeId));
    }


    /// <summary>
    /// Create new trade
    /// </summary>
    /// <remarks>
    /// New trade request is described by the filling following fields:
    /// - **ClientId** (optional) - Client trade Id
    /// - **Type** (required) - Type of trade. Possible values: `"Market"`, `"Limit"`, `"Stop"`
    /// - **Side** (required) - Side of trade. Possible values: `"Buy"`, `"Sell"`
    /// - **Symbol** (required) - Trade symbol (e.g. `"EURUSD"`)
    /// - **Price** (optional) - Price of the `"Limit"` / `"Stop"` trades (for `Market` trades price field is ignored)
    /// - **Amount** (required) - Trade amount
    /// - **StopLoss** (optional) - Stop loss price
    /// - **TakeProfit** (optional) - Take profit price
    /// - **ExpiredTimestamp** (optional) - Expiration date and time for pending trades (`"Limit"`, `"Stop"`)
    /// - **ImmediateOrCancel** (optional) - "Immediate or cancel" flag (works only for `"Limit"` trades)
    /// - **Comment** (optional) - Client comment
    /// </remarks>
    /// <param name="request">Create trade request</param>
    /// <returns>Created trade</returns>
    public Future<HttpResponse> CreateTradeAsyncStart(TTTradeCreate request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpPostAsyncStart(String.format("api/v1/trade"), request);
    }

    public TTTrade CreateTradeAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTrade.class, httpResponseFuture);
    }

    public TTTrade CreateTrade(TTTradeCreate request) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return CreateTradeAsyncEnd(CreateTradeAsyncStart(request));
    }

    /// <summary>
    /// Modify existing trade
    /// </summary>
    /// <remarks>
    /// Modify trade request is described by the filling following fields:
    /// - **Id** (required) - Trade Id
    /// - **Price** (optional) - New price of the `Limit` / `Stop` trades (price of `Market` trades cannot be changed)
    /// - **StopLoss** (optional) - Stop loss price
    /// - **TakeProfit** (optional) - Take profit price
    /// - **ExpiredTimestamp** (optional) - Expiration date and time for pending trades (`Limit`, `Stop`)
    /// - **Comment** (optional) - Client comment
    /// </remarks>
    /// <param name="request">Modify trade request</param>
    /// <returns>Modified trade</returns>
    public Future<HttpResponse> ModifyTradeAsyncStart(TTTradeModify request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpPutAsyncStart(String.format("api/v1/trade"), request);
    }

    public TTTrade ModifyTradeAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTrade.class, httpResponseFuture);
    }

    public TTTrade ModifyTrade(TTTradeModify request) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return ModifyTradeAsyncEnd(ModifyTradeAsyncStart(request));
    }

    /// <summary>
    /// Cancel existing pending trade
    /// </summary>
    /// <param name="tradeId">Trade Id to cancel</param>
    public Future<HttpResponse> CancelTradeAsyncStart(long tradeId) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpDeleteAsyncStart(String.format("api/v1/trade?type=Cancel&id=%1$s", tradeId));
    }

    public TTTradeDelete CancelTradeAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTradeDelete.class, httpResponseFuture);
    }

    public TTTradeDelete CancelTrade(long tradeId) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return CancelTradeAsyncEnd(CancelTradeAsyncStart(tradeId));
    }


    /// <summary>
    /// Close existing market trade
    /// </summary>
    /// <param name="tradeId">Trade Id to close</param>
    /// <param name="amount">Amount to close (optional)</param>
    public Future<HttpResponse> CloseTradeAsyncStart(long tradeId, BigDecimal amount) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpDeleteAsyncStart(amount != null ? String.format("api/v1/trade?type=Close&id=%1$s&amount=%2$s", tradeId, amount) : String.format("api/v1/trade?type=Close&id=%1$s", tradeId));
    }

    public TTTradeDelete CloseTradeAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTradeDelete.class, httpResponseFuture);
    }

    public TTTradeDelete CloseTrade(long tradeId, BigDecimal amount) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return CancelTradeAsyncEnd(CancelTradeAsyncStart(tradeId));
    }


    /// <summary>
    /// Close existing market trade by another one
    /// </summary>
    /// <param name="tradeId">Trade Id to close</param>
    /// <param name="byTradeId">By trade Id</param>
    public Future<HttpResponse> CloseByTradeAsyncStart(long tradeId, long byTradeId) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpDeleteAsyncStart(String.format("api/v1/trade?type=CloseBy&id=%1$s&byid=%2$s", tradeId, byTradeId));
    }

    public TTTradeDelete CloseByTradeAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTradeDelete.class, httpResponseFuture);
    }

    public TTTradeDelete CloseByTrade(long tradeId, long byTradeId) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return CancelTradeAsyncEnd(CancelTradeAsyncStart(tradeId));
    }


    /// <summary>
    /// Get account trade history
    /// </summary>
    /// <remarks>
    /// New trade history request is described by the filling following fields:
    /// - **TimestampFrom** (optional) - Lower timestamp bound of the trade history request
    /// - **TimestampTo** (optional) - Upper timestamp bound of the trade history request
    /// - **RequestDirection** (optional) - Request paging direction ("Forward" or "Backward"). Default is "Forward".
    /// - **RequestFromId** (optional) - Request paging from Id
    ///
    /// If timestamps fields are not set trade history will be requests from the begin or from the current timestamp
    /// depending on **RequestDirection** value.
    ///
    /// Trade history is returned by chunks by paging size (default is 100). You can provide timestamp bounds (from, to)
    /// and direction of access (forward or backward). After the first request you'll get a list of trade history
    /// records with Ids. The next request should contain **RequestFromId** with the Id of the last processed trade
    /// history record. As the result you'll get the next chunk of trade history records. If the last page was reached
    /// response flag **IsLastReport** will be set.
    /// </remarks>
    /// <param name="request">Trade history request</param>
    /// <returns>Trade history report</returns>
    public Future<HttpResponse> GetTradeHistoryAsyncStart(TTTradeHistoryRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return PrivateHttpPostAsyncStart("api/v1/tradehistory", request);
    }

    public TTTradeHistoryReport GetTradeHistoryAsyncEnd(Future<HttpResponse> httpResponseFuture) throws ExecutionException, InterruptedException, IOException {
        return HttpRequestEnd(TTTradeHistoryReport.class, httpResponseFuture);
    }

    public TTTradeHistoryReport GetTradeHistory(TTTradeHistoryRequest request) throws InterruptedException, ExecutionException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return GetTradeHistoryAsyncEnd(GetTradeHistoryAsyncStart(request));
    }

}

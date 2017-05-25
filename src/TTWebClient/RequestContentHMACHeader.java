package TTWebClient;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

import java.util.Date;

/**
 * Created by yahor.subach on 5/24/2017.
 */
public class RequestContentHMACHeader implements Header {

    public RequestContentHMACHeader(String webApiId, String webApiKey, String webApiSecret){
        String message = "Account Web API methods requeies valid Web API token (Id, Key, Secret)!";
        if (webApiId==null && webApiId=="")
            throw new IllegalArgumentException(message+" illegal webApiId");
        if (webApiKey==null && webApiKey=="")
            throw new IllegalArgumentException(message+" illegal webApiKey");
        if (webApiSecret==null && webApiSecret=="")
            throw new IllegalArgumentException(message+" illegal webApiSecret");

        long timestamp = new Date(System.currentTimeMillis()).getTime();

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        return new HeaderElement[0];
    }
}

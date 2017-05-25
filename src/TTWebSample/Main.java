package TTWebSample;

import TTWebClient.Domain.*;
import TTWebClient.Domain.Enums.TTAccountingTypes;
import TTWebClient.Domain.Enums.TTOrderSides;
import TTWebClient.Domain.Enums.TTOrderTypes;
import TTWebClient.Domain.Enums.TTStreamingDirections;
import TTWebClient.TickTraderWebClient;

import java.io.Console;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.ConsoleHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        TickTraderWebClient client = new TickTraderWebClient("https://tp.st.soft-fx.eu:8443", "ee3b2c50-9f79-473d-b637-0f1b1ba7db6c",
                "hndk7nbmQAHyh25P", "GscxkbMme4BbmSKnp87ES6rewfyXp2zEFEk4SgCWJSb6bkCRZKw98SRKa8adMZ8n");
        client.IgnoreServerCertificate();
        System.out.println("--- Public Web API methods ---");

        GetPublicTradeSession(client);

        GetPublicCurrencies(client);
        GetPublicSymbols(client);
        GetPublicTicks(client);
        GetPublicTicksLevel2(client);

        System.out.println("--- Web API client methods ---");

        GetAccount(client);
        GetTradeSession(client);

        GetCurrencies(client);
        GetSymbols(client);
        GetTicks(client);
        GetTicksLevel2(client);

        GetAssets(client);
        GetPositions(client);
        GetTrades(client);
        GetTradeHistory(client);

        LimitOrder(client);
        client.close();
        
        return;
    }

    public static void GetPublicTradeSession(TickTraderWebClient client) throws Exception {
        // Public trade session
        TTTradeSession publictradesession = client.GetPublicTradeSession();
        System.out.println(String.format("TickTrader name: %1$s", publictradesession.PlatformName));
        System.out.println(String.format("TickTrader company: %1$s", publictradesession.PlatformCompany));
        System.out.println(String.format("TickTrader address: %1$s", publictradesession.PlatformAddress));
        System.out.println(String.format("TickTrader timezone offset: %1$s", publictradesession.PlatformTimezoneOffset));
        System.out.println(String.format("TickTrader session status: %1$s", publictradesession.SessionStatus));
    }


    public static void GetPublicCurrencies(TickTraderWebClient client) throws InterruptedException, ExecutionException, IOException {
        // Public currencies
        List<TTCurrency> publicCurrencies = client.GetPublicAllCurrencies();
        for (TTCurrency c : publicCurrencies) {
            System.out.println("Currency: " + c.Name);
        }

        TTCurrency publicCurrency = client.GetPublicCurrency(publicCurrencies.get(0).Name).get(0);
        if (publicCurrency != null)
            System.out.println(String.format("%1$s currency precision: %2$s", publicCurrency.Name, publicCurrency.Precision));
    }

    public static void GetPublicSymbols(TickTraderWebClient client) throws InterruptedException, ExecutionException, IOException {
        // Public symbols
        ArrayList<TTSymbol> publicSymbols = client.GetPublicAllSymbols();
        for (TTSymbol s : publicSymbols) {
            System.out.println("Symbol: " + s.Symbol);
        }

        TTSymbol publicSymbol = client.GetPublicSymbol(publicSymbols.get(0).Symbol).get(0);
        if (publicSymbol != null)
            System.out.println(String.format("%1$s symbol precision: %2$s", publicSymbol.Symbol, publicSymbol.Precision));
    }

    public static void GetPublicTicks(TickTraderWebClient client) throws InterruptedException, ExecutionException, IOException {

        // Public feed ticks
        ArrayList<TTFeedTick> publicTicks = client.GetPublicAllTicks();
        for (TTFeedTick t : publicTicks) {
            System.out.println(String.format("%1$s tick: %2$s, %3$s", t.Symbol, t.BestBid == null ? "null" : t.BestBid.Price, t.BestAsk == null ? "null" : t.BestAsk.Price));
        }

        TTFeedTick publicTick = client.GetPublicTick(publicTicks.get(0).Symbol).get(0);
        if (publicTick != null)
            System.out.println(String.format("%1$s tick timestamp: %2$s", publicTick.Symbol, publicTick.Timestamp));

    }

    public static void GetPublicTicksLevel2(TickTraderWebClient client) throws InterruptedException, ExecutionException, IOException {
        // Public feed ticks level2
        ArrayList<TTFeedTickLevel2> publicTicksLevel2 = client.GetPublicAllTicksLevel2();
        for (TTFeedTickLevel2 t : publicTicksLevel2) {
            System.out.println(String.format("%1$s level2 book depth: %2$s", t.Symbol, Math.max(t.Bids.size(), t.Asks.size())));
        }

        TTFeedTickLevel2 publicTickLevel2 = client.GetPublicTickLevel2(publicTicksLevel2.get(0).Symbol).get(0);
        if (publicTickLevel2 != null)
            System.out.println(String.format("%1$s level2 book depth: %2$s", publicTickLevel2.Symbol, Math.max(publicTickLevel2.Bids.size(), publicTickLevel2.Asks.size())));

    }

    public static void GetPublicTickers(TickTraderWebClient client) throws InterruptedException, ExecutionException, IOException {
        // Public symbol statistics
        // Public feed ticks level2
        ArrayList<TTTicker> publicTickers = client.GetPublicAllTickers();
        for (TTTicker t : publicTickers) {
            System.out.println(String.format("%1$s last buy/sell prices : %2$s / %3$s", t.Symbol, t.LastBuyPrice, t.LastSellPrice));
        }

        TTTicker publicTicker = client.GetPublicTicker(publicTickers.get(0).Symbol).get(0);
        if (publicTicker != null)
            System.out.println(String.format("%1$s best bid/ask: %2$s / %3$s", publicTicker.Symbol, publicTicker.BestBid, publicTicker.BestAsk));

    }


    public static void GetAccount(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        // Account info
        TTAccount account = client.GetAccount();
        System.out.println(String.format("Account Id: %1$s", account.Id));
        System.out.println(String.format("Account name: %1$s", account.Name));
        System.out.println(String.format("Account group: %1$s", account.Group));
    }

    public static void GetTradeSession(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Account trade session
        TTTradeSession tradesession = client.GetTradeSession();
        System.out.println(String.format("Trade session status: %1$s", tradesession.SessionStatus));
    }

    public static void GetCurrencies(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Account currencies
        ArrayList<TTCurrency> currencies = client.GetAllCurrencies();
        for (TTCurrency c : currencies)
            System.out.println("Currency: " + c.Name);

        TTCurrency currency = client.GetCurrency(currencies.get(0).Name).get(0);
        if (currency != null)
            System.out.println(String.format("%1$s currency precision: %2$s", currency.Name, currency.Precision));
    }

    public static void GetSymbols(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Account symbols
        ArrayList<TTSymbol> symbols = client.GetAllSymbols();
        for (TTSymbol s : symbols)
            System.out.println("Symbol: " + s.Symbol);

        TTSymbol symbol = client.GetSymbol(symbols.get(0).Symbol).get(0);
        if (symbol != null)
            System.out.println(String.format("%1$s symbol precision: %2$s", symbol.Symbol, symbol.Precision));
    }


    public static void GetTicks(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        // Account feed ticks
        ArrayList<TTFeedTick> ticks = client.GetAllTicks();
        for (TTFeedTick t : ticks)
            System.out.println(String.format("%1$s tick: %2$s, %3$s", t.Symbol, t.BestBid != null ? t.BestBid.Price : 0, t.BestAsk != null ? t.BestAsk.Price : 0));

        TTFeedTick tick = client.GetTick(ticks.get(0).Symbol).get(0);
        if (tick != null)
            System.out.println(String.format("%1$s tick timestamp: %2$s", tick.Symbol, tick.Timestamp));
    }


    public static void GetTicksLevel2(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Account feed ticks level2
        ArrayList<TTFeedTickLevel2> ticksLevel2 = client.GetAllTicksLevel2();
        for (TTFeedTickLevel2 t : ticksLevel2)
            System.out.println(String.format("%1$s level2 book depth: %2$s", t.Symbol, Math.max(t.Bids.size(), t.Asks.size())));

        TTFeedTickLevel2 tickLevel2 = client.GetTickLevel2(ticksLevel2.get(0).Symbol).get(0);
        if (tickLevel2 != null)
            System.out.println(String.format("%1$s} level2 book depth: %2$s", tickLevel2.Symbol, tickLevel2.Timestamp));
    }

    public static void GetAssets(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Account assets
        TTAccount account = client.GetAccount();
        if (account.AccountingType == TTAccountingTypes.Cash) {
            ArrayList<TTAsset> assets = client.GetAllAssets();
            for (TTAsset a : assets) {
                System.out.println(String.format("%1$s asset: %2$s", a.Currency, a.Amount));
            }
        }
    }

    public static void GetPositions(TickTraderWebClient client)  throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException
    {
        // Account positions
        TTAccount account = client.GetAccount();
        if (account.AccountingType == TTAccountingTypes.Net)
        {
            ArrayList<TTPosition> positions = client.GetAllPositions();
            for (TTPosition p : positions)
                System.out.println(String.format("%1$s position: %2$s %3$s", p.Symbol, p.LongAmount, p.ShortAmount));
        }
    }

    public static void GetTrades(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Account trades
        ArrayList<TTTrade> trades = client.GetAllTrades();
        for (TTTrade t : trades)
            System.out.println(String.format("%1$s trade with type %2$s by symbol %3$s: %4$s", t.Id, t.Type, t.Symbol, t.Amount));

        TTTrade trade = client.GetTrade(trades.get(0).Id);
        System.out.println(String.format("%1$s trade with type %2$s by symbol %3$s: %4$s", trade.Id, trade.Type, trade.Symbol, trade.Amount));
    }

    public static void GetTradeHistory(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        int iterations = 3;
        TTTradeHistoryRequest request = new TTTradeHistoryRequest();
        request.TimestampTo = new Date();
        request.RequestDirection = TTStreamingDirections.Backward;
        request.RequestPageSize = 10;
        // Try to get trade history from now to the past. Request is limited to 30 records!
        while (iterations-- > 0)
        {
            TTTradeHistoryReport report = client.GetTradeHistory(request);
            for (TTTradeHistory record : report.Records){
                System.out.println(String.format("TradeHistory record: Id=%1$s, TransactionType=%2$s, TransactionReason=%3$s, Symbol=%4$s, TradeId=%5$s", record.Id, record.TransactionType, record.TransactionReason, record.Symbol, record.TradeId));
                request.RequestLastId = record.Id;
            }

            // Stop for last report
            if (report.IsLastReport)
                break;
        }
    }


    public static void LimitOrder(TickTraderWebClient client) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Create, modify and cancel limit order
        TTAccount account = client.GetAccount();
        if ((account.AccountingType == TTAccountingTypes.Gross) || (account.AccountingType == TTAccountingTypes.Net)) {
            // Create limit order
            TTTradeCreate ttTradeCreate = new TTTradeCreate();
            ttTradeCreate.Type = TTOrderTypes.Limit;
            ttTradeCreate.Side = TTOrderSides.Buy;
            ttTradeCreate.Symbol = (account.AccountingType == TTAccountingTypes.Gross) ? "EURUSD" : "EUR/USD";
            ttTradeCreate.Amount = new BigDecimal(10000);
            ttTradeCreate.Price = new BigDecimal(1.0);
            ttTradeCreate.Comment = "Buy limit from Web API sample";
            TTTrade limit = client.CreateTrade(ttTradeCreate);

            // Modify limit order
            TTTradeModify ttTradeModify = new TTTradeModify();
            ttTradeModify.Id = limit.Id;
            ttTradeModify.Comment = "Modified limit from Web API sample";
            limit = client.ModifyTrade(ttTradeModify);

            // Cancel limit order
            client.CancelTrade(limit.Id);
        }
    }

}

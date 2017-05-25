package TTWebClient.Domain;

import java.util.Date;
import java.util.ArrayList;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTFeedTickLevel2 {
    /// <summary>Symbol name</summary>
    public String Symbol;

    /// <summary>Timestamp</summary>
    public Date Timestamp;

    /// <summary>Best bid value</summary>
    public TTFeedLevel2Record BestBid;

    /// <summary>Best ask value</summary>
    public TTFeedLevel2Record BestAsk;

    /// <summary>Bid book</summary>
    public ArrayList<TTFeedLevel2Record> Bids;

    /// <summary>Ask book</summary>
    public ArrayList<TTFeedLevel2Record> Asks;
}

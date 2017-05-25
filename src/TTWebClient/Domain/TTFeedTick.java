package TTWebClient.Domain;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTFeedTick {
    /// <summary>Symbol name</summary>
    public String Symbol;

    /// <summary>Timestamp</summary>
    public Date Timestamp;

    /// <summary>Best bid value</summary>
    public TTFeedLevel2Record BestBid;

    /// <summary>Best ask value</summary>
    public TTFeedLevel2Record BestAsk;
}

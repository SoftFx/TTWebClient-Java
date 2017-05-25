package TTWebClient.Domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTicker {
    /// <summary>Symbol name</summary>
    public String Symbol;

    /// <summary>Best bid</summary>
    public BigDecimal BestBid;

    /// <summary>Best ask</summary>
    public BigDecimal BestAsk;

    /// <summary>Last buy price</summary>
    public BigDecimal LastBuyPrice;

    /// <summary>Last buy volume</summary>
    public BigDecimal LastBuyVolume;

    /// <summary>Last buy timestamp</summary>
    public Date LastBuyTimestamp;

    /// <summary>Last sell price</summary>
    public BigDecimal LastSellPrice;

    /// <summary>Last sell volume</summary>
    public BigDecimal LastSellVolume;

    /// <summary>Last sell timestamp</summary>
    public Date LastSellTimestamp;

    /// <summary>Daily best buy price</summary>
    public BigDecimal DailyBestBuyPrice;

    /// <summary>Daily best sell price</summary>
    public BigDecimal DailyBestSellPrice;

    /// <summary>Daily traded buy volume</summary>
    public BigDecimal DailyTradedBuyVolume;

    /// <summary>Daily traded sell volume</summary>
    public BigDecimal DailyTradedSellVolume;

    /// <summary>Daily traded total volume</summary>
    public BigDecimal DailyTradedTotalVolume;
}

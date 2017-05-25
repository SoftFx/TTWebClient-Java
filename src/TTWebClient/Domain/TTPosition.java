package TTWebClient.Domain;

import java.math.BigDecimal;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTPosition {
    /// <summary>Position Id</summary>
    public long Id;

    /// <summary>Position symbol</summary>
    public String Symbol;

    /// <summary>Position long (buy) amount</summary>
    public BigDecimal LongAmount;

    /// <summary>Position long (buy) price</summary>
    public BigDecimal LongPrice;

    /// <summary>Position short (sell) amount</summary>
    public BigDecimal ShortAmount;

    /// <summary>Position short (sell) price</summary>
    public BigDecimal ShortPrice;

    /// <summary>Commission</summary>
    public BigDecimal Commission;

    /// <summary>Agent commission</summary>
    public BigDecimal AgentCommission;

    /// <summary>Swap</summary>
    public BigDecimal Swap;
}

package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTOrderSides;
import TTWebClient.Domain.Enums.TTOrderStatuses;
import TTWebClient.Domain.Enums.TTOrderTypes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTrade {
    /// <summary>Trade Id</summary>
    public long Id ;

    /// <summary>Trade client Id</summary>
    public String ClientId ;

    /// <summary>Account Id</summary>
    public long AccountId ;

    /// <summary>Trade type</summary>
    public TTOrderTypes Type ;

    /// <summary>Trade initial type</summary>
    public TTOrderTypes InitialType ;

    /// <summary>Trade side</summary>
    public TTOrderSides Side ;

    /// <summary>Trade status</summary>
    public TTOrderStatuses Status ;

    /// <summary>Symbol</summary>
    public String Symbol ;

    /// <summary>Price</summary>
    public BigDecimal Price ;

    /// <summary>Amount</summary>
    public BigDecimal Amount ;

    /// <summary>Initial amount</summary>
    public BigDecimal InitialAmount ;

    /// <summary>Stop loss</summary>
    public BigDecimal StopLoss ;

    /// <summary>Take profit</summary>
    public BigDecimal TakeProfit ;

    /// <summary>Margin</summary>
    public BigDecimal Margin ;

    /// <summary>Commission</summary>
    public BigDecimal Commission ;

    /// <summary>Agent commission</summary>
    public BigDecimal AgentCommission ;

    /// <summary>Swap</summary>
    public BigDecimal Swap ;

    /// <summary>Created timestamp</summary>
    public Date Created ;

    /// <summary>Expired timestamp</summary>
    public Date Expired ;

    /// <summary>Modified timestamp</summary>
    public Date Modified ;

    /// <summary>Filled timestamp</summary>
    public Date Filled ;

    /// <summary>Position created timestamp</summary>
    public Date PositionCreated ;

    /// <summary>Comment</summary>
    public String Comment ;
}

package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTOrderSides;
import TTWebClient.Domain.Enums.TTOrderTypes;
import TTWebClient.Domain.Enums.TTTradeTransReasons;
import TTWebClient.Domain.Enums.TTTradeTransTypes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeHistory {
    public String Id;

    /// <summary>Trade transaction type</summary>
    public TTTradeTransTypes TransactionType;

    /// <summary>Trade transaction reason</summary>
    public TTTradeTransReasons TransactionReason;

    /// <summary>Trade transaction timestamp</summary>
    public Date TransactionTimestamp;

    /// <summary>Symbol</summary>
    public String Symbol;

    /// <summary>Trade Id</summary>
    public long TradeId;

    /// <summary>Parent trade Id</summary>
    public Long ParentTradeId;

    /// <summary>Client trade Id</summary>
    public String ClientTradeId;

    /// <summary>Trade side</summary>
    public TTOrderSides TradeSide;

    /// <summary>Trade type</summary>
    public TTOrderTypes TradeType;

    /// <summary>Trade created timestamp</summary>
    public Date TradeCreated;

    /// <summary>Trade modified timestamp</summary>
    public Date TradeModified;

    /// <summary>Trade amount</summary>
    public BigDecimal TradeAmount;

    /// <summary>Trade initial amount</summary>
    public BigDecimal TradeInitialAmount;

    /// <summary>Trade last fill amount</summary>
    public BigDecimal TradeLastFillAmount;

    /// <summary>Trade price</summary>
    public BigDecimal TradePrice;

    /// <summary>Trade fill price</summary>
    public BigDecimal TradeFillPrice;

    /// <summary>Request price</summary>
    public BigDecimal RequestPrice;

    /// <summary>Request timestamp</summary>
    public Date RequestTimestamp;

    /// <summary>Position Id</summary>
    public Long PositionId;

    /// <summary>Position amount</summary>
    public BigDecimal PositionAmount;

    /// <summary>Position initial amount</summary>
    public BigDecimal PositionInitialAmount;

    /// <summary>Position last amount</summary>
    public BigDecimal PositionLastAmount;

    /// <summary>Position open price</summary>
    public BigDecimal PositionOpenPrice;

    /// <summary>Position opened timestamp</summary>
    public Date PositionOpened;

    /// <summary>Position close price</summary>
    public BigDecimal PositionClosePrice;

    /// <summary>Position closed timestamp</summary>
    public Date PositionClosed;

    /// <summary>Balance value</summary>
    public BigDecimal Balance;

    /// <summary>Balance movement value</summary>
    public BigDecimal BalanceMovement;

    /// <summary>Balance currency</summary>
    public String BalanceCurrency;

    /// <summary>Stop loss</summary>
    public BigDecimal StopLoss;

    /// <summary>Take profit</summary>
    public BigDecimal TakeProfit;

    /// <summary>Commission</summary>
    public BigDecimal Commission;

    /// <summary>Agent commission</summary>
    public BigDecimal AgentCommission;

    /// <summary>Swap</summary>
    public BigDecimal Swap;

    /// <summary>Expired timestamp</summary>
    public Date Expired;

    /// <summary>Comment</summary>
    public String Comment;

    /// <summary>Initial margin rate</summary>
    public BigDecimal MarginRateInitial;

    /// <summary>Open conversion rate</summary>
    public BigDecimal OpenConversionRate;

    /// <summary>Close conversion rate</summary>
    public BigDecimal CloseConversionRate;
}

package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTOrderSides;
import TTWebClient.Domain.Enums.TTOrderTypes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeCreate {
    /// <summary>Trade client Id (optional)</summary>
    public String ClientId;

    /// <summary>Trade type (required)</summary>
    public TTOrderTypes Type;

    /// <summary>Trade side (required)</summary>
    public TTOrderSides Side;

    /// <summary>Symbol (required)</summary>
    public String Symbol;

    /// <summary>Price (optional)</summary>
    public BigDecimal Price;

    /// <summary>Amount (required)</summary>
    public BigDecimal Amount;

    /// <summary>Stop loss (optional)</summary>
    public BigDecimal StopLoss;

    /// <summary>Take profit (optional)</summary>
    public BigDecimal TakeProfit;

    /// <summary>Expired timestamp (optional)</summary>
    public Date Expired;

    /// <summary>Immediate or cancel execution option (optional)</summary>
    public Boolean ImmediateOrCancel;

    /// <summary>Comment (optional)</summary>
    public String Comment;
}

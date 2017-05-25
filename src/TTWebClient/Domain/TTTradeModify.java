package TTWebClient.Domain;

import java.math.BigDecimal;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeModify {
    /// <summary>Trade Id (required)</summary>
    public long Id;

    /// <summary>Price (optional)</summary>
    public BigDecimal Price;

    /// <summary>Stop loss (optional)</summary>
    public BigDecimal StopLoss;

    /// <summary>Take profit (optional)</summary>
    public BigDecimal TakeProfit;

    /// <summary>Expired timestamp (optional)</summary>
    public BigDecimal Expired;

    /// <summary>Comment (optional)</summary>
    public String Comment;
}

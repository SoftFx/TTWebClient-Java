package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTPriceType;

import java.math.BigDecimal;

/**
 * Created by yahor.subach on 5/19/2017.
 */

public class TTFeedLevel2Record {
    /// <summary>Price type</summary>
    public TTPriceType Type;

    /// <summary>Price</summary>
    public BigDecimal Price;

    /// <summary>Volume</summary>
    public BigDecimal Volume;
}

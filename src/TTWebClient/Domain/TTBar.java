package TTWebClient.Domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yahor.subach on 5/29/2017.
 */
/// <summary>
/// Bar
/// </summary>
public class TTBar
{
    /// <summary>Timestamp</summary>
    public Date Timestamp;

    /// <summary>Open price</summary>
    public BigDecimal Open;
    /// <summary>Open price</summary>
    public BigDecimal High;
    /// <summary>Open price</summary>
    public BigDecimal Low;
    /// <summary>Open price</summary>
    public BigDecimal Close;
    /// <summary>Bar volume</summary>
    public BigDecimal Voleme;
}

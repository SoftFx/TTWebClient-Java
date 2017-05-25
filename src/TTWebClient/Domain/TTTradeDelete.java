package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTTradeDeleteTypes;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeDelete {
    /// <summary>Delete trade type</summary>
    public TTTradeDeleteTypes Type;

    /// <summary>Deleted trade</summary>
    public TTTrade Trade;

    /// <summary>By trade (optional)</summary>
    public TTTrade ByTrade;
}

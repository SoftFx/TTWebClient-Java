package TTWebClient.Domain;

import java.util.ArrayList;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeHistoryReport {
    /// <summary>Is report last for paging request?</summary>
    public Boolean IsLastReport;

    /// <summary>List of trade history records</summary>
    public ArrayList<TTTradeHistory> Records;
}

package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTStreamingDirections;

import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeHistoryRequest {
    /// <summary>Lower timestamp bound of the trade history request (optional)</summary>
    public Date TimestampFrom;

    /// <summary>Upper timestamp bound of the trade history request (optional)</summary>
    public Date TimestampTo;

    /// <summary>Request paging direction ("Forward" or "Backward"). Default is "Forward" (optional)</summary>
    public TTStreamingDirections RequestDirection;

    /// <summary>Request paging size. Default is 100 (optional)</summary>
    public Integer RequestPageSize;

    /// <summary>Request paging last Id (optional)</summary>
    public String RequestLastId;
}

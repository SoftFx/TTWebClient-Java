package TTWebClient.Domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yahor.subach on 5/29/2017.
 */
public class TTTicksResponse {
    /// <summary>From time</summary>
    public Date From;
    /// <summary>From time</summary>
    public Date To;

    /// <summary>Returned ticks</summary>
    public ArrayList<TTFeedTickLevel2> Ticks;
}

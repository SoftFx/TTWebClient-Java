package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTTradeTransReasons {

    ClientRequest(0),
    PndOrdAct(1),
    StopOut(2),
    StopLossAct(3),
    TakeProfitAct(4),
    DealerDecision(5),
    Rollover(6),
    Delete(7);

    private final int id;
    TTTradeTransReasons(int id) { this.id = id; }
    public int getValue() { return id; }
}

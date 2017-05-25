package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTProfitCalculationModes
{

    Forex(0),
    CFD(1),
    Futures(2),
    CFD_Index(3),
    CFD_Leverage(4);

    private final int id;
    TTProfitCalculationModes(int id) { this.id = id; }
    public int getValue() { return id; }
}

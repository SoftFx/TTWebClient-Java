package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTTradeTransTypes
{
    OrderOpened(0),
    OrderCanceled(1),
    OrderExpired(2),
    OrderFilled(3),
    PositionClosed(4),
    Balance(5),
    Credit(6),
    PositionOpened(7);

    private final int id;
    TTTradeTransTypes(int id) { this.id = id; }
    public int getValue() { return id; }
}

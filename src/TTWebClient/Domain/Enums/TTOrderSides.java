package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTOrderSides {
    Buy(0),
    Sell(1);

    private final int id;
    TTOrderSides(int id) { this.id = id; }
    public int getValue() { return id; }
}

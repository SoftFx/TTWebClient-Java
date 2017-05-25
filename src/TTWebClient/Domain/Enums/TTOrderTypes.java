package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTOrderTypes {
    Market(0),
    Limit(1),
    Stop(2),
    Position(3);

    private final int id;
    TTOrderTypes(int id) { this.id = id; }
    public int getValue() { return id; }
}

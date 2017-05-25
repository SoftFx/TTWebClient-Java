package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTCommissionValueType {

    Money(0),
    Points(1),
    Percentage(2);

    private final int id;
    TTCommissionValueType(int id) { this.id = id; }
    public int getValue() { return id; }
}

package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTCommissionChargeMethod {
    OneWay(0),
    RoundTurn(1);

    private final int id;
    TTCommissionChargeMethod(int id) { this.id = id; }
    public int getValue() { return id; }
}

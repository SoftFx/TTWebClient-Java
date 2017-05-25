package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTCommissionChargeType {
    PerLot(0),
    PerDeal(1);

    private final int id;
    TTCommissionChargeType(int id) { this.id = id; }
    public int getValue() { return id; }
}

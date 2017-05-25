package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTOrderStatuses {
    New(0),
    Calculated(1),
    Filled(2),
    Canceled(3);

    private final int id;
    TTOrderStatuses(int id) { this.id = id; }
    public int getValue() { return id; }
}

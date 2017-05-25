package TTWebClient.Domain.Enums;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public enum TTStreamingDirections {
    Forward(0),
    Backward(1);

    private final int id;
    TTStreamingDirections(int id) { this.id = id; }
    public int getValue() { return id; }
}

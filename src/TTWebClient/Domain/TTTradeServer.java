package TTWebClient.Domain;

/**
 * Created by yahor.subach on 5/29/2017.
 */
public class TTTradeServer {
    /// <summary>Company name</summary>
    public String CompanyName;
    /// <summary>Company full name</summary>
    public String CompanyFullName;
    /// <summary>Company description</summary>
    public String CompanyDescription;
    /// <summary>Company address</summary>
    public String CompanyAddress;
    /// <summary>Company email address</summary>
    public String CompanyEmail;
    /// <summary>Company phone</summary>
    public String CompanyPhone;
    /// <summary>Company web site</summary>
    public String CompanyWebSite;
    /// <summary>Server name</summary>
    public String ServerName;
    /// <summary>Server full name</summary>
    public String ServerFullName;
    /// <summary>Server description</summary>
    public String ServerDescription;
    /// <summary>Server address</summary>
    public String ServerAddress;
    /// <summary>Feed server fix ssl port</summary>
    public int ServerFixFeedSslPort;
    /// <summary>Trade server fix ssl port</summary>
    public int ServerFixTradeSslPort;
    /// <summary>Feed server websocket ssl port</summary>
    public int ServerWebSocketFeedPort;
    /// <summary>Trade server websocket ssl port</summary>
    public int ServerWebSocketTradePort;
    /// <summary>Server rest port</summary>
    public int ServerRestPort;
}

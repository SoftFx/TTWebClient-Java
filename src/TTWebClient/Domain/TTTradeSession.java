package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTTradeSessionStatus;

import java.util.Date;


/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTTradeSession {
    /// <summary>Tick Trader Server name</summary>
    public String PlatformName;

    /// <summary>Tick Trader Server owner company</summary>
    public String PlatformCompany;

    /// <summary>Tick Trader Server address</summary>
    public String PlatformAddress;

    /// <summary>Tick Trader Server timezone offset in hours from UTC</summary>
    public int PlatformTimezoneOffset;

    /// <summary>Trading session Id</summary>
    /// <remarks>GUID for Trading Session (empty GUID for closed session)</remarks>
    public String SessionId;

    /// <summary>Trade session status</summary>
    /// <remarks>State of the trade session. Possible values: Closed, Opened</remarks>
    public TTTradeSessionStatus SessionStatus;

    /// <summary>Trading session start time</summary>
    /// <remarks>Start time of the current trading session (the same meaning for opened and closed sessions)</remarks>
    public Date SessionStartTime;

    /// <summary>Trading session end time</summary>
    /// <remarks>End time of the current trading session (the same meaning for opened and closed sessions)</remarks>
    public Date SessionEndTime;

    /// <summary>Trading session open time</summary>
    /// <remarks>
    /// Provides the open time of the current trading session in case of current session is opened. 
    /// Provides the open time of the next open trading session in case of current session is closed.
    /// </remarks>
    public Date SessionOpenTime;

    /// <summary>Trading session close time</summary>
    /// <remarks>
    /// Provides the close time of the current trading session in case of current session is opened. 
    /// Provides the close time of the next open trading session in case of current session is closed.
    /// </remarks> 
    public Date SessionCloseTime;
}

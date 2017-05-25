package TTWebClient.Domain;

import TTWebClient.Domain.Enums.TTAccountingTypes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTAccount {
    /// <summary>Account Id</summary>
    public long Id;

    /// <summary>Account group</summary>
    public String Group;

    /// <summary>Accounting type</summary>
    public TTAccountingTypes AccountingType;

    /// <summary>Name</summary>
    public String Name;

    /// <summary>Email</summary>
    public String Email;

    /// <summary>Comment</summary>
    public String Comment;

    /// <summary>Registered timestamp</summary>
    public Date Registered;

    /// <summary>Is account blocked?</summary>
    public Boolean IsBlocked;

    /// <summary>Is account read only?</summary>
    public Boolean IsReadonly;

    /// <summary>Is account in valid state?</summary>
    public Boolean IsValid;

    /// <summary>Is Web API enabled for account?</summary>
    public Boolean IsWebApiEnabled;

    /// <summary>Leverage</summary>
    public Integer Leverage;

    /// <summary>Balance amount</summary>
    public BigDecimal Balance;

    /// <summary>Balance currency</summary>
    public String BalanceCurrency;

    /// <summary>Equity</summary>
    public BigDecimal Equity;

    /// <summary>Margin</summary>
    public BigDecimal Margin;

    /// <summary>Margin level</summary>
    public BigDecimal MarginLevel;

    /// <summary>Margin call level</summary>
    public Integer MarginCallLevel;

    /// <summary>Stop out level</summary>
    public Integer StopOutLevel;
}

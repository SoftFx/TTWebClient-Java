package TTWebClient.Domain;

import TTWebClient.Domain.Enums.*;

import java.math.BigDecimal;

/**
 * Created by yahor.subach on 5/19/2017.
 */
public class TTSymbol {
    /// <summary>Symbol name</summary>
    public String Symbol;

    /// <summary>Symbol precision (digits after BigDecimal point)</summary>
    public int Precision;

    /// <summary>Is trade allowed for the symbol?</summary>
    public Boolean IsTradeAllowed;

    /// <summary>Margin calculation mode</summary>
    public TTMarginCalculationModes MarginMode;

    /// <summary>Profit calculation mode</summary>
    public TTProfitCalculationModes ProfitMode;

    /// <summary>Contract size for the symbol</summary>
    public double ContractSize;

    /// <summary>The factor which is used to calculate margin for hedged orders/positions</summary>
    public double MarginHedged;

    /// <summary>The factor of margin calculation</summary>
    public double MarginFactor;

    /// <summary>Margin currency name</summary>
    public String MarginCurrency;

    /// <summary>Margin currency precision</summary>
    public int MarginCurrencyPrecision;

    /// <summary>Profit currency name</summary>
    public String ProfitCurrency;

    /// <summary>Profit currency precision</summary>
    public int ProfitCurrencyPrecision;

    /// <summary>Symbol description</summary>
    public String Description;

    /// <summary>Is swap enabled for the symbol?</summary>
    public Boolean SwapEnabled;

    /// <summary>Short swap size</summary>
    public float SwapSizeShort;

    /// <summary>Long swap size</summary>
    public float SwapSizeLong;

    /// <summary>Minimal trade amount</summary>
    public BigDecimal MinTradeAmount;

    /// <summary>Maximal trade amount</summary>
    public BigDecimal MaxTradeAmount;

    /// <summary>Trade amount step</summary>
    public BigDecimal TradeAmountStep;

    /// <summary>Commission type</summary>
    public TTCommissionValueType CommissionType;

    /// <summary>Commission charge type</summary>
    public TTCommissionChargeType CommissionChargeType;

    /// <summary>Commission charge method</summary>
    public TTCommissionChargeMethod CommissionChargeMethod;

    /// <summary>Commission</summary>
    public BigDecimal Commission;

    /// <summary>Limits commission</summary>
    public BigDecimal LimitsCommission;
}

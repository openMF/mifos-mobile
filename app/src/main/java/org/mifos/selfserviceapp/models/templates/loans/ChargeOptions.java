package org.mifos.selfserviceapp.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.ChargeCalculationType;
import org.mifos.selfserviceapp.models.ChargeTimeType;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class ChargeOptions implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("active")
    Boolean active;

    @SerializedName("penalty")
    Boolean penalty;

    @SerializedName("currency")
    Currency currency;

    @SerializedName("amount")
    Double amount;

    @SerializedName("chargeTimeType")
    ChargeTimeType chargeTimeType;

    @SerializedName("chargeAppliesTo")
    ChargeAppliesTo chargeAppliesTo;

    @SerializedName("chargeCalculationType")
    ChargeCalculationType chargeCalculationType;

    @SerializedName("chargePaymentMode")
    ChargePaymentMode chargePaymentMode;

    @SerializedName("taxGroup")
    TaxGroup taxGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPenalty() {
        return penalty;
    }

    public void setPenalty(Boolean penalty) {
        this.penalty = penalty;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ChargeTimeType getChargeTimeType() {
        return chargeTimeType;
    }

    public void setChargeTimeType(ChargeTimeType chargeTimeType) {
        this.chargeTimeType = chargeTimeType;
    }

    public ChargeAppliesTo getChargeAppliesTo() {
        return chargeAppliesTo;
    }

    public void setChargeAppliesTo(ChargeAppliesTo chargeAppliesTo) {
        this.chargeAppliesTo = chargeAppliesTo;
    }

    public ChargeCalculationType getChargeCalculationType() {
        return chargeCalculationType;
    }

    public void setChargeCalculationType(ChargeCalculationType chargeCalculationType) {
        this.chargeCalculationType = chargeCalculationType;
    }

    public ChargePaymentMode getChargePaymentMode() {
        return chargePaymentMode;
    }

    public void setChargePaymentMode(ChargePaymentMode chargePaymentMode) {
        this.chargePaymentMode = chargePaymentMode;
    }

    public TaxGroup getTaxGroup() {
        return taxGroup;
    }

    public void setTaxGroup(TaxGroup taxGroup) {
        this.taxGroup = taxGroup;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.active);
        dest.writeValue(this.penalty);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.amount);
        dest.writeParcelable(this.chargeTimeType, flags);
        dest.writeParcelable(this.chargeAppliesTo, flags);
        dest.writeParcelable(this.chargeCalculationType, flags);
        dest.writeParcelable(this.chargePaymentMode, flags);
        dest.writeParcelable(this.taxGroup, flags);
    }

    public ChargeOptions() {
    }

    protected ChargeOptions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.penalty = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.chargeTimeType = in.readParcelable(ChargeTimeType.class.getClassLoader());
        this.chargeAppliesTo = in.readParcelable(ChargeAppliesTo.class.getClassLoader());
        this.chargeCalculationType = in.readParcelable(ChargeCalculationType.class.getClassLoader
                ());
        this.chargePaymentMode = in.readParcelable(ChargePaymentMode.class.getClassLoader());
        this.taxGroup = in.readParcelable(TaxGroup.class.getClassLoader());
    }

    public static final Creator<ChargeOptions> CREATOR =
            new Creator<ChargeOptions>() {
        @Override
        public ChargeOptions createFromParcel(Parcel source) {
            return new ChargeOptions(source);
        }

        @Override
        public ChargeOptions[] newArray(int size) {
            return new ChargeOptions[size];
        }
    };
}

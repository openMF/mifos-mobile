package org.mifos.selfserviceapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.client.Currency;
import org.mifos.selfserviceapp.models.client.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 10/8/16.
 */
public class Transaction implements Parcelable {

    @SerializedName("id")
    Long id;

    @SerializedName("officeId")
    Long officeId;

    @SerializedName("officeName")
    String officeName;

    @SerializedName("type")
    Type type;

    @SerializedName("date")
    List<Integer> date = new ArrayList<>();

    @SerializedName("currency")
    Currency currency;

    @SerializedName("amount")
    Double amount;

    @SerializedName("submittedOnDate")
    List<Integer> submittedOnDate = new ArrayList<>();

    @SerializedName("reversed")
    Boolean reversed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Integer> getDate() {
        return date;
    }

    public void setDate(List<Integer> date) {
        this.date = date;
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

    public List<Integer> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(List<Integer> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.officeId);
        dest.writeString(this.officeName);
        dest.writeParcelable(this.type, flags);
        dest.writeList(this.date);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.amount);
        dest.writeList(this.submittedOnDate);
        dest.writeValue(this.reversed);
    }

    public Transaction() {
    }

    protected Transaction(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.officeId = (Long) in.readValue(Long.class.getClassLoader());
        this.officeName = in.readString();
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.date = new ArrayList<Integer>();
        in.readList(this.date, Integer.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.submittedOnDate = new ArrayList<Integer>();
        in.readList(this.submittedOnDate, Integer.class.getClassLoader());
        this.reversed = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Transaction> CREATOR =
            new Parcelable.Creator<Transaction>() {
                @Override
                public Transaction createFromParcel(Parcel source) {
                    return new Transaction(source);
                }

                @Override
                public Transaction[] newArray(int size) {
                    return new Transaction[size];
                }
            };
}
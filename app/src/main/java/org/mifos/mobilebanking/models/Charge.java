package org.mifos.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.mifos.mobilebanking.api.local.SelfServiceDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
@Table(database = SelfServiceDatabase.class)
public class Charge extends BaseModel implements Parcelable {

    @PrimaryKey
    Integer id;

    Integer clientId;
    Integer chargeId;

    @Column
    String name;

    List<Integer> dueDate = new ArrayList<>();
    ChargeTimeType chargeTimeType;
    ChargeCalculationType chargeCalculationType;
    Currency currency;

    @Column
    Double amount = 0.0;

    @Column
    Double amountPaid = 0.0;

    @Column
    Double amountWaived = 0.0;

    @Column
    Double amountWrittenOff = 0.0;

    @Column
    Double amountOutstanding = 0.0;

    Boolean penalty = false;

    @Column
    Boolean isActive = false;

    Boolean isPaid = false;
    Boolean isWaived = false;

    Boolean paid = false;
    Boolean waived = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public int getClientId() {
        return chargeId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountWaived() {
        return amountWaived;
    }

    public void setAmountWaived(double amountWaived) {
        this.amountWaived = amountWaived;
    }

    public double getAmountWrittenOff() {
        return amountWrittenOff;
    }

    public void setAmountWrittenOff(double amountWrittenOff) {
        this.amountWrittenOff = amountWrittenOff;
    }

    public double getAmountOutstanding() {
        return amountOutstanding;
    }

    public void setAmountOutstanding(double amountOutstanding) {
        this.amountOutstanding = amountOutstanding;
    }

    public List<Integer> getDueDate() {
        return dueDate;
    }

    public void setDueDate(List<Integer> dueDate) {
        this.dueDate = dueDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public ChargeTimeType getChargeTimeType() {
        return chargeTimeType;
    }

    public void setChargeTimeType(ChargeTimeType chargeTimeType) {
        this.chargeTimeType = chargeTimeType;
    }

    public ChargeCalculationType getChargeCalculationType() {
        return chargeCalculationType;
    }

    public void setChargeCalculationType(ChargeCalculationType chargeCalculationType) {
        this.chargeCalculationType = chargeCalculationType;
    }

    public boolean isPenalty() {
        return penalty;
    }

    public void setPenalty(boolean penalty) {
        this.penalty = penalty;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean isIsWaived() {
        return isWaived;
    }

    public void setWaived(boolean isWaived) {
        this.isWaived = isWaived;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getWaived() {
        return waived;
    }

    public void setWaived(Boolean waived) {
        this.waived = waived;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.clientId);
        dest.writeValue(this.chargeId);
        dest.writeString(this.name);
        dest.writeList(this.dueDate);
        dest.writeParcelable(this.chargeTimeType, flags);
        dest.writeParcelable(this.chargeCalculationType, flags);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.amount);
        dest.writeValue(this.amountPaid);
        dest.writeValue(this.amountWaived);
        dest.writeValue(this.amountWrittenOff);
        dest.writeValue(this.amountOutstanding);
        dest.writeValue(this.penalty);
        dest.writeValue(this.isActive);
        dest.writeValue(this.isPaid);
        dest.writeValue(this.isWaived);
        dest.writeValue(this.paid);
        dest.writeValue(this.waived);
    }

    public Charge() {
    }

    protected Charge(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clientId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.chargeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.dueDate = new ArrayList<>();
        in.readList(this.dueDate, Integer.class.getClassLoader());
        this.chargeTimeType = in.readParcelable(ChargeTimeType.class.getClassLoader());
        this.chargeCalculationType = in.readParcelable(ChargeCalculationType.class.
                getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.amountPaid = (Double) in.readValue(Double.class.getClassLoader());
        this.amountWaived = (Double) in.readValue(Double.class.getClassLoader());
        this.amountWrittenOff = (Double) in.readValue(Double.class.getClassLoader());
        this.amountOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.penalty = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isPaid = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isWaived = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.paid = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.waived = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Charge> CREATOR = new Parcelable.Creator<Charge>() {
        @Override
        public Charge createFromParcel(Parcel source) {
            return new Charge(source);
        }

        @Override
        public Charge[] newArray(int size) {
            return new Charge[size];
        }
    };
}

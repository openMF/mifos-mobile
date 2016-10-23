package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Status implements Parcelable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("code")
    private String code;

    @SerializedName("value")
    private String value;

    @SerializedName("pendingApproval")
    private Boolean pendingApproval;

    @SerializedName("waitingForDisbursal")
    private Boolean waitingForDisbursal;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("closedObligationsMet")
    private Boolean closedObligationsMet;

    @SerializedName("closedWrittenOff")
    private Boolean closedWrittenOff;

    @SerializedName("closedRescheduled")
    private Boolean closedRescheduled;

    @SerializedName("closed")
    private Boolean closed;

    @SerializedName("overpaid")
    private Boolean overpaid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Status withCode(String code) {
        this.code = code;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Status withValue(String value) {
        this.value = value;
        return this;
    }

    public Boolean getPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(Boolean pendingApproval) {
        this.pendingApproval = pendingApproval;
    }

    public Status withPendingApproval(Boolean pendingApproval) {
        this.pendingApproval = pendingApproval;
        return this;
    }

    public Boolean getWaitingForDisbursal() {
        return waitingForDisbursal;
    }

    public void setWaitingForDisbursal(Boolean waitingForDisbursal) {
        this.waitingForDisbursal = waitingForDisbursal;
    }

    public Status withWaitingForDisbursal(Boolean waitingForDisbursal) {
        this.waitingForDisbursal = waitingForDisbursal;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Status withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getClosedObligationsMet() {
        return closedObligationsMet;
    }

    public void setClosedObligationsMet(Boolean closedObligationsMet) {
        this.closedObligationsMet = closedObligationsMet;
    }

    public Status withClosedObligationsMet(Boolean closedObligationsMet) {
        this.closedObligationsMet = closedObligationsMet;
        return this;
    }

    public Boolean getClosedWrittenOff() {
        return closedWrittenOff;
    }

    public void setClosedWrittenOff(Boolean closedWrittenOff) {
        this.closedWrittenOff = closedWrittenOff;
    }

    public Status withClosedWrittenOff(Boolean closedWrittenOff) {
        this.closedWrittenOff = closedWrittenOff;
        return this;
    }

    public Boolean getClosedRescheduled() {
        return closedRescheduled;
    }

    public void setClosedRescheduled(Boolean closedRescheduled) {
        this.closedRescheduled = closedRescheduled;
    }

    public Status withClosedRescheduled(Boolean closedRescheduled) {
        this.closedRescheduled = closedRescheduled;
        return this;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Status withClosed(Boolean closed) {
        this.closed = closed;
        return this;
    }

    public Boolean getOverpaid() {
        return overpaid;
    }

    public void setOverpaid(Boolean overpaid) {
        this.overpaid = overpaid;
    }

    public Status withOverpaid(Boolean overpaid) {
        this.overpaid = overpaid;
        return this;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", pendingApproval=" + pendingApproval +
                ", waitingForDisbursal=" + waitingForDisbursal +
                ", active=" + active +
                ", closedObligationsMet=" + closedObligationsMet +
                ", closedWrittenOff=" + closedWrittenOff +
                ", closedRescheduled=" + closedRescheduled +
                ", closed=" + closed +
                ", overpaid=" + overpaid +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.code);
        dest.writeString(this.value);
        dest.writeValue(this.pendingApproval);
        dest.writeValue(this.waitingForDisbursal);
        dest.writeValue(this.active);
        dest.writeValue(this.closedObligationsMet);
        dest.writeValue(this.closedWrittenOff);
        dest.writeValue(this.closedRescheduled);
        dest.writeValue(this.closed);
        dest.writeValue(this.overpaid);
    }

    public Status() {
    }

    protected Status(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.value = in.readString();
        this.pendingApproval = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.waitingForDisbursal = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.closedObligationsMet = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.closedWrittenOff = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.closedRescheduled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.closed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overpaid = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel source) {
            return new Status(source);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}

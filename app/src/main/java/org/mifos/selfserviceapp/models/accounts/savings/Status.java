package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Status implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("code")
    String code;

    @SerializedName("value")
    String value;

    @SerializedName("submittedAndPendingApproval")
    Boolean submittedAndPendingApproval;

    @SerializedName("approved")
    Boolean approved;

    @SerializedName("rejected")
    Boolean rejected;

    @SerializedName("withdrawnByApplicant")
    Boolean withdrawnByApplicant;

    @SerializedName("active")
    Boolean active;

    @SerializedName("closed")
    Boolean closed;


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

    public Boolean getSubmittedAndPendingApproval() {
        return submittedAndPendingApproval;
    }

    public void setSubmittedAndPendingApproval(Boolean submittedAndPendingApproval) {
        this.submittedAndPendingApproval = submittedAndPendingApproval;
    }

    public Status withSubmittedAndPendingApproval(Boolean submittedAndPendingApproval) {
        this.submittedAndPendingApproval = submittedAndPendingApproval;
        return this;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Status withApproved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Status withRejected(Boolean rejected) {
        this.rejected = rejected;
        return this;
    }

    public Boolean getWithdrawnByApplicant() {
        return withdrawnByApplicant;
    }

    public void setWithdrawnByApplicant(Boolean withdrawnByApplicant) {
        this.withdrawnByApplicant = withdrawnByApplicant;
    }

    public Status withWithdrawnByApplicant(Boolean withdrawnByApplicant) {
        this.withdrawnByApplicant = withdrawnByApplicant;
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

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", submittedAndPendingApproval=" + submittedAndPendingApproval +
                ", approved=" + approved +
                ", rejected=" + rejected +
                ", withdrawnByApplicant=" + withdrawnByApplicant +
                ", active=" + active +
                ", closed=" + closed +
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
        dest.writeValue(this.submittedAndPendingApproval);
        dest.writeValue(this.approved);
        dest.writeValue(this.rejected);
        dest.writeValue(this.withdrawnByApplicant);
        dest.writeValue(this.active);
        dest.writeValue(this.closed);
    }

    public Status() {
    }

    protected Status(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.value = in.readString();
        this.submittedAndPendingApproval = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.approved = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.rejected = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.withdrawnByApplicant = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.closed = (Boolean) in.readValue(Boolean.class.getClassLoader());
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
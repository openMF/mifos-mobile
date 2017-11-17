package org.mifos.mobilebanking.models.accounts.share;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("submittedAndPendingApproval")
    @Expose
    private Boolean submittedAndPendingApproval;
    @SerializedName("approved")
    @Expose
    private Boolean approved;
    @SerializedName("rejected")
    @Expose
    private Boolean rejected;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("closed")
    @Expose
    private Boolean closed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getSubmittedAndPendingApproval() {
        return submittedAndPendingApproval;
    }

    public void setSubmittedAndPendingApproval(Boolean submittedAndPendingApproval) {
        this.submittedAndPendingApproval = submittedAndPendingApproval;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
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

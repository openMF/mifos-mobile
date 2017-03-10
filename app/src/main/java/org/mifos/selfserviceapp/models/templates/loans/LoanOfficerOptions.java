package org.mifos.selfserviceapp.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class LoanOfficerOptions implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("firstname")
    String firstname;

    @SerializedName("lastname")
    String lastname;

    @SerializedName("displayName")
    String displayName;

    @SerializedName("mobileNo")
    String mobileNo;

    @SerializedName("officeId")
    Integer officeId;

    @SerializedName("officeName")
    String officeName;

    @SerializedName("isLoanOfficer")
    Boolean isLoanOfficer;

    @SerializedName("isActive")
    Boolean isActive;

    @SerializedName("joiningDate")
    List<Integer> joiningDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Boolean getLoanOfficer() {
        return isLoanOfficer;
    }

    public void setLoanOfficer(Boolean loanOfficer) {
        isLoanOfficer = loanOfficer;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Integer> getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(List<Integer> joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Override
    public String toString() {
        return "LoanOfficerOptions{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", displayName='" + displayName + '\'' +
                ", mobileNo=" + mobileNo +
                ", officeId=" + officeId +
                ", officeName=" + officeName +
                ", isLoanOfficer=" + isLoanOfficer +
                ", isActive=" + isActive +
                ", joiningDate=" + joiningDate +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeString(this.displayName);
        dest.writeValue(this.mobileNo);
        dest.writeValue(this.officeId);
        dest.writeValue(this.officeName);
        dest.writeValue(this.isLoanOfficer);
        dest.writeValue(this.isActive);
        dest.writeList(this.joiningDate);
    }

    public LoanOfficerOptions() {
    }

    protected LoanOfficerOptions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.displayName = in.readString();
        this.mobileNo = (String) in.readValue(String.class.getClassLoader());
        this.officeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.officeName = (String) in.readValue(String.class.getClassLoader());
        this.isLoanOfficer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.joiningDate = new ArrayList<Integer>();
        in.readList(this.joiningDate, Integer.class.getClassLoader());
    }

    public static final Creator<LoanOfficerOptions> CREATOR =
            new Creator<LoanOfficerOptions>() {
        @Override
        public LoanOfficerOptions createFromParcel(Parcel source) {
            return new LoanOfficerOptions(source);
        }

        @Override
        public LoanOfficerOptions[] newArray(int size) {
            return new LoanOfficerOptions[size];
        }
    };
}

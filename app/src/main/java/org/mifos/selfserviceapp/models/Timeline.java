package org.mifos.selfserviceapp.models;

/**
 * Created by ishankhanna for mifos android-client on 09/02/14.
 * Created by michaelsosnick on 1/20/17.
 */

/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.util.ArrayList;
        import java.util.List;

public class Timeline implements Parcelable {

    public static final Parcelable.Creator<Timeline> CREATOR = new Parcelable.Creator<Timeline>() {
        @Override
        public Timeline createFromParcel(Parcel source) {
            return new Timeline(source);
        }

        @Override
        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };
    private List<Integer> submittedOnDate = new ArrayList<Integer>();
    private String submittedByUsername;
    private String submittedByFirstname;
    private String submittedByLastname;
    private List<Integer> activatedOnDate = new ArrayList<Integer>();
    private String activatedByUsername;
    private String activatedByFirstname;
    private String activatedByLastname;
    private List<Integer> closedOnDate = new ArrayList<Integer>();
    private String closedByUsername;
    private String closedByFirstname;
    private String closedByLastname;

    public Timeline() {
    }

    protected Timeline(Parcel in) {
        this.submittedOnDate = new ArrayList<Integer>();
        in.readList(this.submittedOnDate, Integer.class.getClassLoader());
        this.submittedByUsername = in.readString();
        this.submittedByFirstname = in.readString();
        this.submittedByLastname = in.readString();
        this.activatedOnDate = new ArrayList<Integer>();
        in.readList(this.activatedOnDate, Integer.class.getClassLoader());
        this.activatedByUsername = in.readString();
        this.activatedByFirstname = in.readString();
        this.activatedByLastname = in.readString();
        this.closedOnDate = new ArrayList<Integer>();
        in.readList(this.closedOnDate, Integer.class.getClassLoader());
        this.closedByUsername = in.readString();
        this.closedByFirstname = in.readString();
        this.closedByLastname = in.readString();
    }

    public String getSubmittedByUsername() {
        return this.submittedByUsername;
    }

    public void setSubmittedByUsername(String submittedByUsername) {
        this.submittedByUsername = submittedByUsername;
    }

    public String getSubmittedByFirstname() {
        return this.submittedByFirstname;
    }

    public void setSubmittedByFirstname(String submittedByFirstname) {
        this.submittedByFirstname = submittedByFirstname;
    }

    public String getSubmittedByLastname() {
        return this.submittedByLastname;
    }

    public void setSubmittedByLastname(String submittedByLastname) {
        this.submittedByLastname = submittedByLastname;
    }

    public String getActivatedByUsername() {
        return this.activatedByUsername;
    }

    public void setActivatedByUsername(String activatedByUsername) {
        this.activatedByUsername = activatedByUsername;
    }

    public String getActivatedByFirstname() {
        return this.activatedByFirstname;
    }

    public void setActivatedByFirstname(String activatedByFirstname) {
        this.activatedByFirstname = activatedByFirstname;
    }

    public String getActivatedByLastname() {
        return this.activatedByLastname;
    }

    public void setActivatedByLastname(String activatedByLastname) {
        this.activatedByLastname = activatedByLastname;
    }

    public List<Integer> getClosedOnDate() {
        return closedOnDate;
    }

    public void setClosedOnDate(List<Integer> closedOnDate) {
        this.closedOnDate = closedOnDate;
    }

    public String getClosedByUsername() {
        return closedByUsername;
    }

    public void setClosedByUsername(String closedByUsername) {
        this.closedByUsername = closedByUsername;
    }

    public String getClosedByFirstname() {
        return closedByFirstname;
    }

    public void setClosedByFirstname(String closedByFirstname) {
        this.closedByFirstname = closedByFirstname;
    }

    public String getClosedByLastname() {
        return closedByLastname;
    }

    public void setClosedByLastname(String closedByLastname) {
        this.closedByLastname = closedByLastname;
    }

    public List<Integer> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(List<Integer> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public List<Integer> getActivatedOnDate() {
        return activatedOnDate;
    }

    public void setActivatedOnDate(List<Integer> activatedOnDate) {
        this.activatedOnDate = activatedOnDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.submittedOnDate);
        dest.writeString(this.submittedByUsername);
        dest.writeString(this.submittedByFirstname);
        dest.writeString(this.submittedByLastname);
        dest.writeList(this.activatedOnDate);
        dest.writeString(this.activatedByUsername);
        dest.writeString(this.activatedByFirstname);
        dest.writeString(this.activatedByLastname);
        dest.writeList(this.closedOnDate);
        dest.writeString(this.closedByUsername);
        dest.writeString(this.closedByFirstname);
        dest.writeString(this.closedByLastname);
    }
}

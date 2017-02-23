package org.mifos.selfserviceapp.models.accounts.share;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timeline {

    @SerializedName("submittedOnDate")
    @Expose
    private List<Integer> submittedOnDate = null;
    @SerializedName("submittedByUsername")
    @Expose
    private String submittedByUsername;
    @SerializedName("submittedByFirstname")
    @Expose
    private String submittedByFirstname;
    @SerializedName("submittedByLastname")
    @Expose
    private String submittedByLastname;
    @SerializedName("approvedDate")
    @Expose
    private List<Integer> approvedDate = null;
    @SerializedName("approvedByUsername")
    @Expose
    private String approvedByUsername;
    @SerializedName("approvedByFirstname")
    @Expose
    private String approvedByFirstname;
    @SerializedName("approvedByLastname")
    @Expose
    private String approvedByLastname;
    @SerializedName("activatedDate")
    @Expose
    private List<Integer> activatedDate = null;
    @SerializedName("activatedByUsername")
    @Expose
    private String activatedByUsername;
    @SerializedName("activatedByFirstname")
    @Expose
    private String activatedByFirstname;
    @SerializedName("activatedByLastname")
    @Expose
    private String activatedByLastname;

    public List<Integer> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(List<Integer> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public String getSubmittedByUsername() {
        return submittedByUsername;
    }

    public void setSubmittedByUsername(String submittedByUsername) {
        this.submittedByUsername = submittedByUsername;
    }

    public String getSubmittedByFirstname() {
        return submittedByFirstname;
    }

    public void setSubmittedByFirstname(String submittedByFirstname) {
        this.submittedByFirstname = submittedByFirstname;
    }

    public String getSubmittedByLastname() {
        return submittedByLastname;
    }

    public void setSubmittedByLastname(String submittedByLastname) {
        this.submittedByLastname = submittedByLastname;
    }

    public List<Integer> getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(List<Integer> approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApprovedByUsername() {
        return approvedByUsername;
    }

    public void setApprovedByUsername(String approvedByUsername) {
        this.approvedByUsername = approvedByUsername;
    }

    public String getApprovedByFirstname() {
        return approvedByFirstname;
    }

    public void setApprovedByFirstname(String approvedByFirstname) {
        this.approvedByFirstname = approvedByFirstname;
    }

    public String getApprovedByLastname() {
        return approvedByLastname;
    }

    public void setApprovedByLastname(String approvedByLastname) {
        this.approvedByLastname = approvedByLastname;
    }

    public List<Integer> getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(List<Integer> activatedDate) {
        this.activatedDate = activatedDate;
    }

    public String getActivatedByUsername() {
        return activatedByUsername;
    }

    public void setActivatedByUsername(String activatedByUsername) {
        this.activatedByUsername = activatedByUsername;
    }

    public String getActivatedByFirstname() {
        return activatedByFirstname;
    }

    public void setActivatedByFirstname(String activatedByFirstname) {
        this.activatedByFirstname = activatedByFirstname;
    }

    public String getActivatedByLastname() {
        return activatedByLastname;
    }

    public void setActivatedByLastname(String activatedByLastname) {
        this.activatedByLastname = activatedByLastname;
    }

}

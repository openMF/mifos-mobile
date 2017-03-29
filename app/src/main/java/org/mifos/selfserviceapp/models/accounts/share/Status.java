package org.mifos.selfserviceapp.models.accounts.share;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

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

}

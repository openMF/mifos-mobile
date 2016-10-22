package org.mifos.selfserviceapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 20/06/16
 */
public class Client {

    private long id;
    private String accountNo;
    private List<Long> activationDate = new ArrayList<>();
    private List<Long> dobDate = new ArrayList<>();
    private String firstname;
    private String middlename;
    private String lastname;
    private String displayName;
    private String fullname;
    private List<Client> pageItems = new ArrayList<Client>();

    public List<Client> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<Client> pageItems) {
        this.pageItems = pageItems;
    }

    public List<Long> getDobDate() {
        return dobDate;
    }

    public void setDobDate(List<Long> dobDate) {
        this.dobDate = dobDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public List<Long> getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(List<Long> activationDate) {
        this.activationDate = activationDate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", accountNo='" + accountNo + '\'' +
                ", activationDate=" + activationDate +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", lastname='" + lastname + '\'' +
                ", displayName='" + displayName + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }

}

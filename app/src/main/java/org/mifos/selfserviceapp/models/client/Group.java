package org.mifos.selfserviceapp.models.client;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 10/7/17.
 */

public class Group {

    @SerializedName("id")
    private Integer id;

    @SerializedName("accountNo")
    private String accountNo;

    @SerializedName("name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

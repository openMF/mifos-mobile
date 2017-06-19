package org.mifos.selfserviceapp.models.beneficary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 16/6/17.
 */

public class BeneficiaryUpdatePayload {

    @SerializedName("name")
    String name;

    @SerializedName("transferLimit")
    int transferLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(int transferLimit) {
        this.transferLimit = transferLimit;
    }
}

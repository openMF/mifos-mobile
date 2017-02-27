package org.mifos.selfserviceapp.models.accounts.loan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 27/2/17.
 */

public class Currency {
    @SerializedName("code")
    private String code;

    @SerializedName("displaySymbol")
    private String displaySymbol;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public void setDisplaySymbol(String displaySymbol) {
        this.displaySymbol = displaySymbol;
    }
}

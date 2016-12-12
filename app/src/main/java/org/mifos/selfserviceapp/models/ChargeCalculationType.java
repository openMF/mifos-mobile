package org.mifos.selfserviceapp.models;

/**
 * Created by michaelsosnick on 12/11/16.
 */

public class ChargeCalculationType {
    private int id;
    private String code; // example "chargeCalculationType.flat"
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}

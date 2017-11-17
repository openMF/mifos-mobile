package org.mifos.mobilebanking.models.client;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 10/7/17.
 */

public class ClientClassification {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("mandatory")
    private Boolean mandatory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }
}

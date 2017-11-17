package org.mifos.mobilebanking.models.accounts;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains variables which are used for implementing Comparator and are common to loan,
 * savings and share account.
 * Created by dilpreet on 14/6/17.
 */

public abstract class Account {
    @SerializedName("id")
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

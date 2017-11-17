package org.mifos.mobilebanking.api.local;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = SelfServiceDatabase.NAME, version = SelfServiceDatabase.VERSION)
public class SelfServiceDatabase {

    public static final String NAME = "SelfService"; // we will add the .db extension

    public static final int VERSION = 1;
}

package org.mifos.mobile.models.templates.account

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountOptionsTemplate(

    var fromAccountOptions: List<AccountOption> = ArrayList(),

    var toAccountOptions: List<AccountOption> = ArrayList(),

    var fromAccountTypeOptions: List<AccountType> = ArrayList(),

    var toAccountTypeOptions: List<AccountType> = ArrayList()
) : Parcelable

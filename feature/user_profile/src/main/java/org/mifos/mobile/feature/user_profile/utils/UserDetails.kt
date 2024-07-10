package org.mifos.mobile.feature.user_profile.utils

data class UserDetails(
    val userName: String?,
    val accountNumber: String?,
    val activationDate: String?,
    val officeName: String?,
    val clientType: String?,
    val groups: String?,
    val clientClassification: String?,
    val phoneNumber: String?,
    val dob: String?,
    val gender: String?
) {
    constructor() : this(
        accountNumber = "",
        activationDate = "",
        clientClassification = "",
        clientType = "",
        dob = "",
        gender = "",
        groups = "",
        officeName = "",
        phoneNumber = "",
        userName = ""
    )
}
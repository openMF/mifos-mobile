package org.mifos.mobile.models.register

/**
 * Created by dilpreet on 31/7/17.
 */

data class RegisterPayload (

    var username: String? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var email: String? = null,

    var mobileNumber: String? = null,

    var accountNumber: String? = null,

    var password: String? = null,

    var authenticationMode: String? = null
)

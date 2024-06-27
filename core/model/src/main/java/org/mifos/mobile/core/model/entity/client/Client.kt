package org.mifos.mobile.core.model.entity.client

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.Timeline

/**
 * @author Vishwajeet
 * @since 20/06/16
 */
@Parcelize
data class Client(
    var id: Int = 0,

    var accountNo: String? = null,

    private var status: Status? = null,

    private var active: Boolean? = null,

    var activationDate: List<Int> = ArrayList(),

    var dobDate: List<Int> = ArrayList(),

    var firstname: String? = null,

    var middlename: String? = null,

    var lastname: String? = null,

    var displayName: String? = null,

    var fullname: String? = null,

    private var officeId: Int? = null,

    var officeName: String? = null,

    private var staffId: Int? = null,

    private var staffName: String? = null,

    private var timeline: Timeline? = null,

    var imageId: Int = 0,

    var isImagePresent: Boolean = false,

    private var externalId: String? = null,

    var mobileNo: String? = null,

    var clientClassification: ClientClassification? = null,

    var clientType: ClientType? = null,

    var gender: Gender? = null,

    var groups: List<Group> = ArrayList(),
) : Parcelable

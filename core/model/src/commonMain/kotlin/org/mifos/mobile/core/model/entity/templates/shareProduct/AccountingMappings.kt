package org.mifos.mobile.core.model.entity.templates.shareProduct

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class AccountingMappings(
    val shareReferenceId: GlAccount? = null,
    val incomeFromFeeAccountId: GlAccount? = null,
    val shareEquityId: GlAccount? = null,
    val shareSuspenseId: GlAccount? = null
) : Parcelable
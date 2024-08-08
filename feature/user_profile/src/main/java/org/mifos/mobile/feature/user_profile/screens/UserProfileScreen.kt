package org.mifos.mobile.feature.user_profile.screens

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.Group
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.component.UserProfileField
import org.mifos.mobile.core.ui.component.UserProfileTopBar
import org.mifos.mobile.feature.third.party.user_profile.R
import org.mifos.mobile.feature.user_profile.utils.TextDrawable
import org.mifos.mobile.feature.user_profile.utils.UserDetails
import org.mifos.mobile.feature.user_profile.viewmodel.UserDetailUiState
import org.mifos.mobile.feature.user_profile.viewmodel.UserDetailViewModel

/**
 * @author pratyush
 * @since 20/12/2023
 */

@Composable
fun UserProfileScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    changePassword: () -> Unit,
) {

    val uiState by viewModel.userDetailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.loadUserData()
    }

    UserProfileScreen(
        navigateBack = navigateBack,
        uiState = uiState,
        onRetry = {
            viewModel.loadUserData()
        },
        changePassword = changePassword,
    )
}

@Composable
fun UserProfileScreen(
    navigateBack: () -> Unit,
    uiState: UserDetailUiState,
    onRetry: () -> Unit,
    changePassword: () -> Unit,
) {
    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    MFScaffold(
        topBar = {
            UserProfileTopBar(home = navigateBack, text = R.string.user_details)
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        scaffoldContent = { paddingValues ->

        when (uiState) {
            is UserDetailUiState.ShowError -> {

                MifosErrorComponent(
                    isNetworkConnected = Network.isConnected(context),
                    isRetryEnabled = true,
                    onRetry = onRetry,
                    message = context.getString(uiState.message)
                )

                LaunchedEffect(true) {
                    snackBarHostState.showSnackbar(
                        message = context.getString(uiState.message),
                        actionLabel = "Ok",
                        duration = SnackbarDuration.Long
                    )
                }
            }

            is UserDetailUiState.Loading -> {
                MifosProgressIndicatorOverlay()
            }

            is UserDetailUiState.ShowUserDetails -> {
                UserProfileContent(
                    changePassword = changePassword,
                    client = uiState.client,
                    bitmap = getUserImage(bitmap = uiState.image, context = context)
                )
            }
        }
        }
    )
}

@Composable
fun UserProfileContent(
    changePassword: () -> Unit,
    client : Client,
    bitmap: Bitmap
) {
    val context = LocalContext.current

    val userName = nullFieldCheck(context.getString(R.string.username), client.displayName, context)
    val accountNumber = nullFieldCheck(context.getString(R.string.account_number), client.accountNo, context)
    val activationDate = nullFieldCheck(context.getString(R.string.activation_date), DateHelper.getDateAsString(client.activationDate), context)
    val officeName = nullFieldCheck(context.getString(R.string.office_name), client.officeName, context)
    val clientType = nullFieldCheck(context.getString(R.string.client_type), client.clientType?.name, context)
    val groups = nullFieldCheck(context.getString(R.string.groups), getGroups(client.groups, context), context)
    val clientClassification = client.clientClassification?.name ?: "-"
    val phoneNumber = nullFieldCheck(context.getString(R.string.phone_number), client.mobileNo, context)
    val dob =
        if (client.dobDate.size != 3) {
            context.getString(R.string.no_dob_found)
        } else {
            DateHelper.getDateAsString(client.dobDate)
        }
    val gender = nullFieldCheck(context.getString(R.string.gender), client.gender?.name, context)
    val userDetails = UserDetails(
        userName = userName,
        accountNumber = accountNumber,
        activationDate = activationDate,
        officeName = officeName,
        clientType = clientType,
        groups = groups,
        clientClassification = clientClassification,
        phoneNumber = phoneNumber,
        dob = dob,
        gender = gender
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            MifosUserImage(
                bitmap = bitmap,
                modifier = Modifier.size(100.dp)
            )
        }
        Divider(color = colorResource(R.color.divider))
        userDetails.userName?.let { UserProfileField(label = R.string.username, value = it) }
        userDetails.accountNumber?.let {
            UserProfileField(
                label = R.string.account_number, value = it
            )
        }
        userDetails.activationDate?.let {
            UserProfileField(
                label = R.string.activation_date, value = it
            )
        }
        userDetails.officeName?.let { UserProfileField(label = R.string.office_name, value = it) }
        userDetails.groups?.let { UserProfileField(label = R.string.groups, value = it) }
        userDetails.clientType?.let { UserProfileField(label = R.string.client_type, value = it) }
        userDetails.clientClassification?.let {
            UserProfileField(
                label = R.string.client_classification, value = it
            )
        }
        UserProfileField(text = R.string.change_password,
            icon = R.drawable.ic_keyboard_arrow_right_black_24dp,
            onClick = { changePassword.invoke() })

        UserProfileDetails(userDetails = userDetails)
    }
}

private fun nullFieldCheck(field: String, value: String?, context : Context): String {
    return value
        ?: (context.getString(R.string.no) + context.getString(R.string.blank) + field +
                context.getString(R.string.blank) + context.getString(R.string.found))
}

private fun getGroups(groups: List<Group>?, context: Context): String {
    if (groups?.isEmpty() == true) {
        return context.getString(
            R.string.not_assigned_with_any_group,
        )
    }
    val builder = StringBuilder()
    if (groups != null) {
        for ((_, _, name) in groups) {
            builder.append(context.getString(R.string.string_and_string, name, " | "))
        }
    }
    return builder.toString().substring(0, builder.toString().length - 2)
}


@Composable
private fun getUserImage( bitmap: Bitmap?, context: Context,): Bitmap {
    val preferencesHelper = PreferencesHelper(context)
    var userBitmap = bitmap

    if (userBitmap == null) {
        val textDrawable = TextDrawable.builder()
            .beginConfig()
            .width(100)
            .height(100)
            .toUpperCase()
            .endConfig()
            .buildRound(
                (
                    if (preferencesHelper.clientName.isNullOrEmpty()) {
                        preferencesHelper.userName
                    } else {
                        preferencesHelper.clientName
                    }
                )?.substring(0, 1),
                ContextCompat.getColor(context, R.color.primary),
            )
        userBitmap = textDrawable.toBitmap()
    }

    return userBitmap
}

class UserProfileScreenPreviewProvider : PreviewParameterProvider<UserDetailUiState> {
    val sampleBitmap = BitmapFactory.decodeResource( null, R.drawable.mifos_logo)
    override val values: Sequence<UserDetailUiState>
        get() = sequenceOf(
            UserDetailUiState.Loading,
            UserDetailUiState.ShowError(0),
            UserDetailUiState.ShowUserDetails(Client(), sampleBitmap)
        )
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun UserProfileScreenPreview(
    @PreviewParameter(UserProfileScreenPreviewProvider::class) userDetailUiState: UserDetailUiState
) {
    UserProfileScreen(
        {},
        userDetailUiState,
        {},
        {},
    )
}
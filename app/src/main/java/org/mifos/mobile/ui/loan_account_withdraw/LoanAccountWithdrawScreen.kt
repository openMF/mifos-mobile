package org.mifos.mobile.ui.loan_account_withdraw

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTitleDescSingleLineEqual
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.ui.savings_account_withdraw.ErrorComponent



@Composable
fun LoanAccountWithdrawScreen(
    viewModel: LoanAccountWithdrawViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()
    val loanWithAssociations by viewModel.loanWithAssociations.collectAsStateWithLifecycle()

    LoanAccountWithdrawScreen(
        uiState = uiState,
        loanWithAssociations = loanWithAssociations,
        navigateBack = navigateBack,
        withdraw = { viewModel.withdrawLoanAccount(it) },
    )
}


@Composable
fun LoanAccountWithdrawScreen(
    uiState: LoanAccountWithdrawUiState,
    loanWithAssociations: LoanWithAssociations?,
    navigateBack: () -> Unit,
    withdraw: (String) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MifosTopBar(
            navigateBack = { navigateBack() },
            title = { Text(text = stringResource(id = R.string.withdraw_loan)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier= Modifier.weight(1f)) {
            LoanAccountWithdrawContent(
                loanWithAssociations = loanWithAssociations,
                withdraw = withdraw
            )

            when (uiState) {
                is LoanAccountWithdrawUiState.Loading -> {
                    MifosProgressIndicator(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                    )
                }

                is LoanAccountWithdrawUiState.Success -> {
                    Toast.makeText(context, R.string.loan_application_withdrawn_successfully, Toast.LENGTH_SHORT).show()
                    navigateBack()
                }

                is LoanAccountWithdrawUiState.Error -> {
                    ErrorComponent(errorToast = stringResource(id = uiState.messageId))
                }

                is LoanAccountWithdrawUiState.WithdrawUiReady -> Unit
            }
        }
    }
}

@Composable
fun LoanAccountWithdrawContent(
    loanWithAssociations: LoanWithAssociations?,
    withdraw: (String) -> Unit
) {
    var reason by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        MifosTitleDescSingleLineEqual(
            title = stringResource(id = R.string.client_name),
            description = loanWithAssociations?.clientName ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTitleDescSingleLineEqual(
            title = stringResource(id = R.string.account_number),
            description = loanWithAssociations?.accountNo ?: ""
        )

        Spacer(modifier = Modifier.height(36.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = reason,
            placeholder = {
                Text(text = stringResource(id = R.string.withdraw_loan_reason))
            },
            onValueChange = { reason = it },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            onClick = { withdraw(reason.text) },
            content = {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = stringResource(id = R.string.withdraw_loan),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
    }
}


class UiStatesParameterProvider : PreviewParameterProvider<LoanAccountWithdrawUiState> {
    override val values: Sequence<LoanAccountWithdrawUiState>
        get() = sequenceOf(
            LoanAccountWithdrawUiState.WithdrawUiReady,
            LoanAccountWithdrawUiState.Error(messageId = R.string.something_went_wrong),
            LoanAccountWithdrawUiState.Loading,
        )
}

@Preview(showSystemUi = true)
@Composable
fun LoanAccountWithdrawPreview(
    @PreviewParameter(UiStatesParameterProvider::class) loanAccountWithdrawUiState: LoanAccountWithdrawUiState
) {
    MifosMobileTheme {
        LoanAccountWithdrawScreen(
            uiState = loanAccountWithdrawUiState,
            loanWithAssociations = LoanWithAssociations(
                clientName = "Mifos Mobile",
                accountNo = "0001"
            ),
            navigateBack = {},
            withdraw = {},
        )
    }
}



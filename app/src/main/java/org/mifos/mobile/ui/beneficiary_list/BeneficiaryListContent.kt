package org.mifos.mobile.ui.beneficiary_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary


@Composable
fun ShowBeneficiary(
    beneficiaryList: List<Beneficiary?>?,
    onClick: (position: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            itemsIndexed(beneficiaryList ?: emptyList()) { index, beneficiary ->
                beneficiary?.let {
                    BeneficiaryItem(beneficiary.name, onClick = {
                        onClick(index)
                    })
                }
            }
        }
    }
}

@Composable
fun BeneficiaryItem(
    beneficiaryName: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        if (beneficiaryName != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp), text = beneficiaryName,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ShowBeneficiaryListEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            painter = painterResource(id = R.drawable.ic_error_black_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.no_beneficiary_found_please_add),
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewBeneficiaryListEmpty(modifier: Modifier = Modifier) {
    MifosMobileTheme {
        ShowBeneficiaryListEmpty()
    }
}
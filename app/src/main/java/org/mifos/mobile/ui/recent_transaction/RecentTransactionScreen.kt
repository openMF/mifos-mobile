package org.mifos.mobile.ui.recent_transaction

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.NoInternet

/**
 * @author pratyush
 * @since 03/01/2024
 */

@Composable
fun RecentTransactionScreen(list: List<RecentTransaction>, isOnline: Boolean) {

    if (!isOnline) {
        NoInternet(
            icon = R.drawable.ic_error_black_24dp,
            error = R.string.no_internet_connection
        )
    }

    // list can be empty so you need to show UI for that as well
    if(list.isEmpty()) {
        //show some composable
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list) {
            if (it.amount?.isNotEmpty() == true && it.value?.isNotEmpty() == true) {
                RecentTransactionItem(
                    amount = it.amount,
                    date = it.date,
                    value = it.value
                )
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RecentTransactionScreenPreview() {
    RecentTransactionScreen(emptyList(), false)
}
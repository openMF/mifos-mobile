package org.mifos.mobile.ui.help

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FaqItemHolder
import org.mifos.mobile.core.ui.component.MifosTextButtonWithTopDrawable
import org.mifos.mobile.core.ui.component.MifosTitleSearchCard
import org.mifos.mobile.core.ui.component.MifosTopBar


@Composable
fun HelpScreen(
    faqArrayList: List<FAQ?>?,
    selectedFaqPosition: Int = -1,
    callNow: () -> Unit,
    leaveEmail: () -> Unit,
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    searchQuery: (String) -> Unit,
    onSearchDismiss: () -> Unit,
    updateFaqPosition: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MifosTopBar(
            navigateBack = navigateBack,
            title = {
                MifosTitleSearchCard(
                    searchQuery = searchQuery,
                    titleResourceId = R.string.help,
                    onSearchDismiss = onSearchDismiss
                )
            }
        )

        Text(
            text = stringResource(id = R.string.faq),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )

        if (!faqArrayList.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = faqArrayList) { index, faqItem ->
                    FaqItemHolder(
                        question = faqItem?.question,
                        answer = faqItem?.answer,
                        isSelected = selectedFaqPosition == index,
                        onItemSelected = { updateFaqPosition(it) },
                        index = index
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier
                        .weight(1f),
                    onClick = callNow,
                    textResourceId = R.string.call_now,
                    icon = Icons.Default.Phone,
                    contentDescription = "Phone Icon"
                )
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = leaveEmail,
                    textResourceId = R.string.leave_email,
                    icon = Icons.Outlined.Mail,
                    contentDescription = "Mail Icon"
                )
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = findLocations,
                    textResourceId = R.string.find_locations,
                    icon = Icons.Default.LocationOn,
                    contentDescription = "Location Icon"
                )
            }
        } else {
            EmptyDataView(
                modifier = Modifier.fillMaxSize(),
                icon = R.drawable.ic_help_black_24dp,
                error = R.string.no_questions_found
            )
        }

    }
}


package org.mifos.mobile.ui.help

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.ui.component.MifosSearchTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    searchQuery: (String) -> Unit,
    titleResourceId: Int,
    onSearchDismiss: () -> Unit
) {
    TopAppBar(
        modifier = Modifier,
        title = {
            HomeTopBarTitle(
                searchQuery = searchQuery,
                titleResourceId = titleResourceId,
                onSearchDismiss = onSearchDismiss
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack.invoke() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme())
                Color(0xFF1B1B1F)
            else
                Color(0xFFFEFBFF)
        ),
    )
}

@Composable
private fun HomeTopBarTitle(
    searchQuery: (String) -> Unit,
    titleResourceId: Int,
    onSearchDismiss: () -> Unit
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchedQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    if (!isSearching) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = titleResourceId),
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            IconButton(onClick = { isSearching = true }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MifosSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchedQuery,
                onValueChange = {
                    searchedQuery = it
                    searchQuery(it.text)
                },
                onSearchDismiss = {
                    searchedQuery = TextFieldValue("")
                    isSearching = false
                    onSearchDismiss.invoke()
                },
            )
        }
    }
}

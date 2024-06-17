package org.mifos.mobile.ui.client_accounts

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosSearchTextField
import org.mifos.mobile.utils.Constants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientAccountsScreen(navigateBack: () -> Unit?) {

    var Query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    MFScaffold(
        topBar = { ClientAccountsScreenTopBar(
            navigateBack= navigateBack,
            Query = Query,
            onChange = { Query = it })
        },

        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = {},
            contentColor = if (isSystemInDarkTheme()) Color.Red else Color.Red,
            content = { Icon(Icons.Filled.Add, contentDescription = "Create Account", tint = if (isSystemInDarkTheme()) Color.White else Color.Black) }
        ),
        scaffoldContent = {

            val tabs = listOf("Savings", "Loan", "Share")
            Column(){
                TabRowComponent(
                    tabs = tabs,
                    modifier = Modifier.padding(it),
                    navigateBack
                )
            }
        }
    )
}

@Composable
fun ClientAccountsScreenTopBar(
    navigateBack: () -> Unit?,
    Query: TextFieldValue,
    onChange: (TextFieldValue) -> Unit
)
{
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    Row(
        Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(50.dp),  horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

        IconButton(
            onClick = { navigateBack.invoke() },
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Arrow",
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(), contentAlignment = Alignment.CenterStart ){

            Text("Accounts",
                style = MaterialTheme.typography.titleLarge,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically)
            {

                IconButton(
                    onClick = { isSearchActive = true },
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),

                    ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Add account",
                        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                }
                IconButton(
                    onClick = { /* Handle button click */ },
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                        contentDescription = "Add account"
                    )
                }
            }

            if(isSearchActive)
            {
                MifosSearchTextField(
                    value = Query,
                    onValueChange = {
                        onChange(it)
                    },
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    onSearchDismiss = {
                        isSearchActive = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ResourceAsColor")
@Composable
fun TabRowComponent(
    tabs: List<String>,
    modifier: Modifier,
    navigateBack: () -> Unit?
) {
    var pagerState = rememberPagerState(pageCount = { 3 })
    var currentPage by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if(!pagerState.isScrollInProgress)
        currentPage = pagerState.currentPage
        else {
            currentPage = pagerState.targetPage
        }
    }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.md_theme_dark_surfaceTint) else colorResource(id = R.color.md_theme_light_surfaceTintColor),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[currentPage])
                        .padding(start = 36.dp, end = 36.dp),
                    color = if (isSystemInDarkTheme()) colorResource(id = R.color.md_theme_dark_surfaceTint) else colorResource(id = R.color.md_theme_light_surfaceTintColor),
                )
            }
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = 16.dp),
                    selectedContentColor = if (isSystemInDarkTheme()) colorResource(id = R.color.md_theme_dark_surfaceTint) else colorResource(id = R.color.md_theme_light_surfaceTintColor),
                    unselectedContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    selected = currentPage == index,
                    onClick = { currentPage = index }
                ) {
                    Text( text = tabTitle)
                }
            }
        }
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth(), pageContent =  { page ->
                    if( currentPage == 0 )
                        AccountsScreen(navigateBack =  navigateBack , Constants.SAVINGS_ACCOUNTS )
                    else if( currentPage == 1)
                        AccountsScreen(navigateBack = navigateBack, Constants.LOAN_ACCOUNTS  )
                    else if( currentPage == 2 )
                        AccountsScreen(navigateBack = navigateBack, Constants.SHARE_ACCOUNTS )
            })

    }
}
@Preview
@Composable
fun ClientAccountsScreenPreview() {
    ClientAccountsScreen( {})
}
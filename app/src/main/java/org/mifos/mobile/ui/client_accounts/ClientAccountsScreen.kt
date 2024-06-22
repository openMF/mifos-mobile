package org.mifos.mobile.ui.client_accounts

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosIcons
import org.mifos.mobile.core.ui.component.MifosSearchTextField
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.utils.Constants

@Composable
fun ClientAccountsScreen(
    navigateBack: () -> Unit?,
    openNextActivity: (currentPage: Int) -> Unit,
    onItemClick: (accountType: String, accountId : Long) -> Unit
) {
    val context = LocalContext.current
    val viewModel: AccountsViewModel = hiltViewModel()
    var isDialogActive by rememberSaveable { mutableStateOf(false) }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = currentPage) {
        viewModel.setFilterList( checkBoxList = emptyList(), currentPage = currentPage, context = context)
    }

 ClientAccountsScreen(
        navigateBack = navigateBack,
        openNextActivity = { index -> openNextActivity.invoke(index) },
        onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
        cancelFilterDialog = { isDialogActive = false },
        clearFilter = {
            viewModel.setFilterList( checkBoxList = emptyList(), currentPage = currentPage, context = context)
            isDialogActive = false
        },
        filterAccounts = {
            viewModel.setFilterList( checkBoxList = it, currentPage = currentPage, context = context)
            isDialogActive = false
        },
        onSearchQueryChange = { viewModel.updateSearchQuery(query = it) },
        openSearch = { isDialogActive = true },
        closeSearch = { viewModel.stoppedSearching() },
        currentPage = currentPage,
        pageChanged = { index -> currentPage = index },
        isDialogActive = isDialogActive,
        filterList = filterList
    )
}

@Composable
fun ClientAccountsScreen(
    navigateBack: () -> Unit?,
    openNextActivity: (currentPage: Int) -> Unit,
    onItemClick: (accountType: String, accountId : Long) -> Unit,
    cancelFilterDialog: () -> Unit,
    clearFilter: () -> Unit,
    filterAccounts: (checkBoxList: List<CheckboxStatus>) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    openSearch: () -> Unit,
    closeSearch: () -> Unit,
    currentPage: Int,
    pageChanged: (index: Int) -> Unit,
    isDialogActive: Boolean,
    filterList : List<CheckboxStatus>

)  {
    val tabs = listOf("Savings Account", "Loan Account", "Share Account")

    if(isDialogActive) {
        ShowFilterDialog(
            filterList = filterList,
            cancelDialog = { cancelFilterDialog.invoke() },
            clearFilter = { clearFilter.invoke() },
            updateFilterList = { list-> filterAccounts(list) },
            title = tabs[currentPage]
        )
    }

    MFScaffold(
        topBar = {
            ClientAccountsScreenTopBar(
                navigateBack = navigateBack,
                onChange = { onSearchQueryChange(it) },
                clickDialog = { openSearch.invoke() },
                closeSearch = { closeSearch.invoke() }
            )
        },

        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = {
                when (currentPage) {
                    0 -> openNextActivity(currentPage)
                    1 -> openNextActivity(currentPage)
                }
            },
            contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            content = {
                Icon(
                    imageVector = MifosIcons.Add,
                    contentDescription = "Create Account",
                    tint = if (isSystemInDarkTheme()) Color.Black else Color.White)
            }
        ),

        scaffoldContent = {
            val tabs = listOf("Savings", "Loan", "Share")
            TabRowComponent(
                tabs = tabs,
                modifier = Modifier.padding(it),
                pageChanged = { index -> pageChanged.invoke(index) },
                onItemClick= { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
            )
        }
    )
}

@Composable
fun ClientAccountsScreenTopBar(
    navigateBack: () -> Unit?,
    onChange: (String) -> Unit,
    clickDialog: () -> Unit,
    closeSearch: () -> Unit,
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    Row(
        Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

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

            Text(
                text = "Accounts",
                style = MaterialTheme.typography.titleLarge,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { isSearchActive = true },
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                    ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Add account",
                        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                }
                IconButton(
                    onClick = { clickDialog.invoke() },
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
                    value = query,
                    onValueChange = {
                        query = it
                        onChange(it.text)
                    },
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .height(52.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    onSearchDismiss = {
                        query = TextFieldValue("")
                        closeSearch.invoke()
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
    pageChanged: (index: Int) -> Unit,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(key1 = currentPage) {
        pageChanged(currentPage)
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
            modifier = Modifier.fillMaxWidth(),
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
            state = pagerState, modifier = Modifier.fillMaxWidth(), pageContent =  { page ->
                when (currentPage) {
                    0 -> AccountsScreen( accountType = Constants.SAVINGS_ACCOUNTS, onItemClick= { accType, accountId -> onItemClick.invoke(accType, accountId) })
                    1 -> AccountsScreen( accountType = Constants.LOAN_ACCOUNTS, onItemClick={ accType, accountId -> onItemClick.invoke(accType, accountId) } )
                    2 -> AccountsScreen( accountType = Constants.SHARE_ACCOUNTS, onItemClick = { accType, accountId -> onItemClick.invoke(accType, accountId) } )
                }
            }
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun ClientAccountsScreenPreview() {

    ClientAccountsScreen(
        navigateBack =  {},
        openNextActivity = { it -> },
        onItemClick = { accountType, accountId ->  },
        cancelFilterDialog = {   },
        clearFilter = { },
        filterAccounts = { },
        onSearchQueryChange = {  },
        openSearch = {  },
        closeSearch = {  },
        currentPage = 0,
        pageChanged = { index ->   },
        isDialogActive = false,
        filterList = listOf()
    )
}
package org.mifos.mobile.ui.client_accounts

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosSearchTextField
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.StatusUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientAccountsScreen(
    navigateBack: () -> Unit?,
    openNextActivity: (currentPage: Int) -> Unit,
    onItemClick: (accountType: String, accountId : Long) -> Unit
)  {


    val context = LocalContext.current
    val viewModel : AccountsViewModel = hiltViewModel()
    var isDialogActive by rememberSaveable { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(0) }
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()
    var list = listOf<String>( Constants.SAVINGS_ACCOUNTS, Constants.LOAN_ACCOUNTS, Constants.SHARE_ACCOUNTS )

    LaunchedEffect(key1 = currentPage) {
        viewModel.setFilterList( emptyList() )
        getCheckBoxList( currentPage, viewModel, context)
    }

    if(isDialogActive)
    {
        ShowFilterDialog(
            filterList = filterList,
            cancelDialog = { isDialogActive = false  },
            clearFilter = {  getCheckBoxList(currentPage, viewModel, context)
                            isDialogActive = false  },
            filter = { filterAccountsIfChanged( checkBoxList =  it, currentPage, viewModel, context )
                        isDialogActive = false  },
            currentPage = currentPage )
    }

    MFScaffold(
        topBar = { ClientAccountsScreenTopBar(
            navigateBack = navigateBack,
            onChange = { viewModel.updateSearchQuery(it) },
            clickDialog = { isDialogActive = true },
            closeSearch = { viewModel.stoppedSearching() }
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
            content = { Icon(Icons.Filled.Add, contentDescription = "Create Account", tint = if (isSystemInDarkTheme()) Color.White else Color.Black) }
        ),
        scaffoldContent = {

            val tabs = listOf("Savings", "Loan", "Share")
            TabRowComponent(
                tabs = tabs,
                modifier = Modifier.padding(it),
                navigateBack= navigateBack,
                pageChanged = { index -> currentPage = index },
                onItemClick= onItemClick
            )
        }
    )
}

fun filterAccountsIfChanged(checkBoxList: List<CheckboxStatus>, currentPage: Int, viewModel: AccountsViewModel, context: Context) {
    var isChanged = false
    for( checkBox in checkBoxList )
    {
        if(checkBox.isChecked)
            isChanged = true
    }
    if(isChanged)
        viewModel.filterAccounts(checkBoxList)
    else {
        getCheckBoxList(currentPage, viewModel, context)
    }
}

fun getCheckBoxList(currentPage: Int, viewModel: AccountsViewModel, context: Context)
{
    if(currentPage == 0 )
         viewModel.setFilterList(StatusUtils.getSavingsAccountStatusList(context))
    else if( currentPage == 1)
         viewModel.setFilterList(StatusUtils.getLoanAccountStatusList(context))
    else if( currentPage == 2 )
         viewModel.setFilterList(StatusUtils.getShareAccountStatusList(context))

}

@Composable
fun ShowFilterDialog(
    cancelDialog: () -> Unit,
    clearFilter: () -> Unit,
    filter: (checkBoxList: List<CheckboxStatus>) -> Unit,
    currentPage: Int,
    filterList: Any
) {

    var checkBoxList : List<CheckboxStatus> = filterList as List<CheckboxStatus>
    val tabs = listOf("Savings Account", "Loan Account", "Share Account")
    AlertDialog(
        onDismissRequest = { cancelDialog.invoke() },
        text = {
            Column {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = "Filter ${tabs[currentPage]}")
                Text(modifier = Modifier.padding(bottom = 16.dp), text = stringResource(R.string.select_you_want))
                MetaDataCard( filterList ) { checkBoxList = it }

                Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween )
                {
                    TextButton(onClick = { clearFilter.invoke() }) {
                        Text(text = stringResource(R.string.clear_filters))
                    }
                    Row {
                        TextButton(onClick = { cancelDialog.invoke() }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        TextButton(onClick = { filter(checkBoxList) }) {
                            Text(text = stringResource(R.string.filter))
                        }
                    }
                }

            }
        },
        confirmButton = {}
    )
}



@Composable
fun MetaDataCard(
    accountStatusList: List<CheckboxStatus>,
    updateList: (List<CheckboxStatus>) -> Unit
) {

    var checkBoxList by rememberSaveable {
        mutableStateOf(accountStatusList)
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState
    ) {
        items( checkBoxList.size ){ index->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            )
            {
                Checkbox(
                    checked = checkBoxList[index].isChecked,
                    onCheckedChange = {
                        val updatedList = checkBoxList.toMutableList()
                        updatedList[index] = checkBoxList[index].copy(isChecked = it)
                        checkBoxList = updatedList
                        updateList(checkBoxList)
                    },
                    colors= CheckboxColors(
                        checkedBoxColor = Color( checkBoxList[index].color),
                        uncheckedBoxColor =  if (isSystemInDarkTheme()) colorResource(id = R.color.gray_light) else colorResource(id = R.color.white) ,
                        checkedCheckmarkColor = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(id = R.color.white),
                        uncheckedCheckmarkColor= colorResource(id = R.color.white),
                        checkedBorderColor = Color( checkBoxList[index].color),
                        uncheckedBorderColor = Color( checkBoxList[index].color ),
                        disabledBorderColor = colorResource(id = R.color.gray_dark),
                        disabledIndeterminateBorderColor = colorResource(id = R.color.gray_dark),
                        disabledCheckedBoxColor= colorResource(id = R.color.black),
                        disabledUncheckedBoxColor= colorResource(id = R.color.black),
                        disabledIndeterminateBoxColor= colorResource(id = R.color.black),
                        disabledUncheckedBorderColor= colorResource(id = R.color.black),
                    )
                )
                Text(
                    text = checkBoxList[index].status!!,
                    color = if (isSystemInDarkTheme()) colorResource(id = R.color.white) else colorResource(id = R.color.black),
                )
            }
        }

    }
}


@Composable
fun ClientAccountsScreenTopBar(
    navigateBack: () -> Unit?,
    onChange: (String) -> Unit,
    clickDialog: () -> Unit,
    closeSearch: () -> Unit,
)
{
    var Query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    Row(
        Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(50.dp),  horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)
    {

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
                    value = Query,
                    onValueChange = {
                        Query = it
                        onChange(it.text)
                    },
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    onSearchDismiss = {
                        Query = TextFieldValue("")
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
    navigateBack: () -> Unit?,
    pageChanged: (index: Int) -> Unit,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    var pagerState = rememberPagerState(pageCount = { 3 })

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
                if( currentPage == 0 )
                    AccountsScreen(navigateBack =  navigateBack , Constants.SAVINGS_ACCOUNTS, onItemClick)
                else if( currentPage == 1)
                    AccountsScreen(navigateBack = navigateBack, Constants.LOAN_ACCOUNTS, onItemClick )
                else if( currentPage == 2 )
                    AccountsScreen(navigateBack = navigateBack, Constants.SHARE_ACCOUNTS, onItemClick )
            }
        )
    }
}
@Preview
@Composable
fun ClientAccountsScreenPreview() {
    ClientAccountsScreen({}, {} , { _, _ -> }  )
}
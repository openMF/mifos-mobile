package org.mifos.mobile.ui.client_accounts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.CheckboxStatus

@Composable
fun ClientAccountFilterDialog(
    cancelDialog: () -> Unit,
    clearFilter: () -> Unit,
    updateFilterList: (checkBoxList: List<CheckboxStatus>) -> Unit,
    title: String,
    filterList: List<CheckboxStatus>
) {
    var checkBoxList : List<CheckboxStatus> = filterList

    AlertDialog(
        onDismissRequest = { cancelDialog.invoke() },
        text = {
            Column {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = "Filter $title")
                Text(modifier = Modifier.padding(bottom = 16.dp), text = stringResource(R.string.select_you_want))

                ClientAccountFilterCheckBox( accountStatusList =  filterList ) { checkBoxList = it }

                Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween )
                {
                    TextButton(onClick = { clearFilter.invoke() }) {
                        Text(text = stringResource(R.string.clear_filters))
                    }
                    Row {
                        TextButton(onClick = { cancelDialog.invoke() }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        TextButton(onClick = { updateFilterList(checkBoxList) }) {
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
fun ClientAccountFilterCheckBox(
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
                modifier = Modifier.fillMaxWidth(),
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
                        updateList.invoke(checkBoxList)
                    },
                    colors= CheckboxColors(
                        checkedBoxColor = Color(checkBoxList[index].color),
                        uncheckedBoxColor =  if (isSystemInDarkTheme()) colorResource(id = R.color.gray_light) else colorResource(id = R.color.white) ,
                        checkedCheckmarkColor = if (isSystemInDarkTheme()) colorResource(id = R.color.black) else colorResource(id = R.color.white),
                        uncheckedCheckmarkColor= colorResource(id = R.color.white),
                        checkedBorderColor = Color(checkBoxList[index].color),
                        uncheckedBorderColor = Color(checkBoxList[index].color ),
                        disabledBorderColor = colorResource(id = R.color.gray_dark),
                        disabledIndeterminateBorderColor = colorResource(id = R.color.gray_dark),
                        disabledCheckedBoxColor= colorResource(id = R.color.black),
                        disabledUncheckedBoxColor= colorResource(id = R.color.black),
                        disabledIndeterminateBoxColor= colorResource(id = R.color.black),
                        disabledUncheckedBorderColor= colorResource(id = R.color.black),
                    )
                )
                Text(
                    text = checkBoxList[index].status ?: "",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}


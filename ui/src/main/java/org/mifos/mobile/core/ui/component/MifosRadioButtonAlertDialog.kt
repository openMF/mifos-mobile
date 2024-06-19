package org.mifos.mobile.core.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun MifosRadioButtonDialog(
    titleResId: Int,
    selectedItem: String,
    items: Array<String>,
    selectItem: (item: String, index: Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest.invoke() }
    ){
        Card {
            Column(modifier = Modifier.padding(20.dp)) {
            Text(text = stringResource(id = titleResId))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                itemsIndexed(items = items) { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                onDismissRequest.invoke()
                                selectItem.invoke(item, index)
                            }
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (item == selectedItem),
                            onClick = {
                                onDismissRequest.invoke()
                                selectItem.invoke(item, index)
                            }
                        )
                        Text(
                            text = item,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
                }
        }
    }
}

@Preview
@Composable
fun PreviewRadioButtonDialog() {
    MifosMobileTheme {
        MifosRadioButtonDialog(
            titleResId = 1,
            items = arrayOf("1", "2", "3"),
            selectedItem = "1",
            onDismissRequest = {  },
            selectItem = { _, _ ->}
        )
    }
}
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.theme.MifosMobileTheme


@Composable
fun MifosCheckBox(
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    text: String,
    checkboxColors: CheckboxColors = CheckboxDefaults.colors(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckChanged.invoke(it) },
            colors = checkboxColors
        )
        Text(text = text)
    }
}

@Preview(showSystemUi = true)
@Composable
fun MifosCheckBoxPreview() {
    MifosMobileTheme {
        MifosCheckBox(
            checked = false,
            onCheckChanged = {},
            text = ""
        )
    }
}
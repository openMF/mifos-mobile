package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun MifosRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    textResId: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick.invoke() }
        )
        Text(text = stringResource(id = textResId))
    }
}

@Preview(showSystemUi = true)
@Composable
fun MifosRadioButtonPreview() {
    MifosMobileTheme {
        MifosRadioButton(
            selected = false,
            onClick = {},
            textResId = 1
        )
    }
}
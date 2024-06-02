package org.mifos.mobile.core.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MifosDropDownTextField(
    optionsList: List<String> = listOf(),
    selectedOption: String? = null,
    labelResId: Int = 0,
    isEnabled: Boolean = true,
    supportingText: String? = null,
    error: Boolean = false,
    onClick: (Int, String) -> Unit,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(key1 = isPressed) {
        if(isPressed) expanded = true && isEnabled
    }

    Box(
        modifier = Modifier.alpha(if (!isEnabled) 0.4f else 1f),
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {  },
            label = { Text(stringResource(id = labelResId)) },
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth(),
            readOnly = true,
            enabled = isEnabled,
            textStyle = MaterialTheme.typography.labelSmall,
            supportingText = { if (error) Text(text = supportingText ?: "") },
            isError = error,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp
                    else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }
        )

        DropdownMenu(
            expanded = expanded && isEnabled,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(max = 200.dp),
            onDismissRequest = { expanded = false },
        ) {
            optionsList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onClick(index, item)
                    },
                    text = { Text(text = item) }
                )
            }
        }
    }
}

@Composable
fun MifosDropDownDoubleTextField(
    modifier: Modifier = Modifier,
    optionsList: List<Pair<String, String>> = listOf(),
    selectedOption: String? = null,
    labelResId: Int = 0,
    isEnabled: Boolean = true,
    supportingText: String? = null,
    error: Boolean = false,
    onClick: (Int, Pair<String, String>) -> Unit,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(key1 = isPressed) {
        if(isPressed) expanded = true && isEnabled
    }

    Box(
        modifier = modifier.alpha(if (!isEnabled) 0.4f else 1f),
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {  },
            label = { Text(stringResource(id = labelResId)) },
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth(),
            readOnly = true,
            enabled = isEnabled,
            textStyle = MaterialTheme.typography.labelSmall,
            supportingText = { if (error) Text(text = supportingText ?: "") },
            isError = error,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp
                    else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }
        )

        DropdownMenu(
            expanded = expanded && isEnabled,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(max = 200.dp),
            onDismissRequest = { expanded = false },
        ) {
            optionsList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onClick(index, item)
                    },
                    text = {
                        Column {
                            Text(text = item.first)
                            Text(text = item.second)
                        }

                    }

                )
            }
        }
    }
}
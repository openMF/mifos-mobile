/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

/**
 * Enhanced radio button component for Mifos Mobile with improved accessibility,
 * animations, and customization options.
 *
 * @param label The text label for the radio button
 * @param selected Whether this radio button is currently selected
 * @param onClick Callback when the radio button is clicked
 * @param modifier Modifier to be applied to the component
 * @param enabled Whether the radio button is enabled
 * @param isError Whether the radio button should show error state
 * @param contentDescription Optional content description for accessibility
 * @param selectedColor Color when selected (defaults to primary blue)
 * @param unselectedColor Color when unselected (defaults to border color)
 * @param errorColor Color when in error state (defaults to error color)
 * @param selectedTextStyle Text style when selected
 * @param unselectedTextStyle Text style when unselected
 * @param borderWidth Width of the border
 * @param animationDurationMs Duration of animations in milliseconds
 */
@Composable
fun MifosRadioButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    contentDescription: String? = null,
    selectedColor: Color = AppColors.primaryBlue,
    unselectedColor: Color = AppColors.borderColor,
    errorColor: Color = MaterialTheme.colorScheme.error,
    selectedTextStyle: TextStyle = MifosTypography.titleSmallEmphasized,
    unselectedTextStyle: TextStyle = MifosTypography.titleSmall,
    borderWidth: Dp = 1.dp,
    animationDurationMs: Int = 200,
) {
    // Animated colors for smooth transitions
    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            isError -> errorColor
            selected -> selectedColor
            else -> AppColors.borderColorOne
        },
        animationSpec = tween(animationDurationMs),
        label = "border_color_animation",
    )

    val animatedRadioColor by animateColorAsState(
        targetValue = when {
            isError -> errorColor
            selected -> selectedColor
            else -> unselectedColor
        },
        animationSpec = tween(animationDurationMs),
        label = "radio_color_animation",
    )

    // Animated scale for selection feedback
    val animatedScale by animateFloatAsState(
        targetValue = if (selected) 1.02f else 1.0f,
        animationSpec = tween(animationDurationMs),
        label = "scale_animation",
    )

    val textStyle = if (selected) selectedTextStyle else unselectedTextStyle
    val interactionSource = remember { MutableInteractionSource() }

    // Semantic state description for accessibility
    val stateDescription = when {
        isError -> "Error state"
        selected -> "Selected"
        else -> "Not selected"
    }

    Box(
        modifier = Modifier
            .scale(animatedScale)
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription ?: "Radio button for $label"
                this.stateDescription = stateDescription
            },
    ) {
        Row(
            modifier = modifier
                .padding(vertical = DesignToken.padding.extraSmall)
                .border(
                    width = borderWidth,
                    color = animatedBorderColor.copy(
                        alpha = if (enabled) 1.0f else 0.6f,
                    ),
                    shape = DesignToken.shapes.medium,
                )
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    role = Role.RadioButton,
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = true,
                        radius = 24.dp,
                    ),
                )
                .padding(DesignToken.padding.large),
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected,
                // Handled by the Row's clickable
                onClick = null,
                enabled = enabled,
                colors = RadioButtonColors(
                    selectedColor = animatedRadioColor,
                    unselectedColor = animatedRadioColor,
                    disabledSelectedColor = animatedRadioColor.copy(alpha = 0.6f),
                    disabledUnselectedColor = animatedRadioColor.copy(alpha = 0.6f),
                ),
                modifier = Modifier.clearAndSetSemantics { },
            )

            Text(
                text = label,
                style = textStyle.copy(
                    color = textStyle.color.copy(
                        alpha = if (enabled) 1.0f else 0.6f,
                    ),
                ),
                modifier = Modifier.clearAndSetSemantics { },
            )
        }
    }
}

/**
 * Radio button group component that manages a collection of radio buttons
 * with mutual exclusion.
 *
 * @param options List of options to display
 * @param selectedOption Currently selected option
 * @param onSelectionChange Callback when selection changes
 * @param modifier Modifier to be applied to the group
 * @param enabled Whether the group is enabled
 * @param isError Whether the group should show error state
 * @param spacing Spacing between radio buttons
 */
@Composable
fun MifosRadioButtonGroup(
    options: List<String>,
    selectedOption: String?,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    spacing: Dp = DesignToken.spacing.medium,
) {
    Column(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        options.forEach { option ->
            MifosRadioButton(
                label = option,
                selected = option == selectedOption,
                onClick = { onSelectionChange(option) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                isError = isError,
                contentDescription = "Select $option",
            )
        }
    }
}

@Preview
@Composable
private fun Enhanced_Radio_Button_Preview() {
    var selectedLanguage by remember { mutableStateOf("Telugu") }
    var selectedPayment by remember { mutableStateOf<String?>(null) }

    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.medium),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
        ) {
            // Individual radio buttons
            Text(
                text = "Individual Radio Buttons",
                style = MifosTypography.headlineMedium,
                modifier = Modifier.padding(bottom = DesignToken.spacing.small),
            )

            MifosRadioButton(
                label = "Telugu (Selected)",
                selected = true,
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            )

            MifosRadioButton(
                label = "English (Unselected)",
                selected = false,
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            )

            MifosRadioButton(
                label = "Hindi (Selected, Disabled)",
                selected = true,
                enabled = false,
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            )

            MifosRadioButton(
                label = "French (Error State)",
                selected = false,
                isError = true,
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.size(DesignToken.spacing.medium))

            // Radio button group
            Text(
                text = "Radio Button Group",
                style = MifosTypography.headlineMedium,
                modifier = Modifier.padding(bottom = DesignToken.spacing.small),
            )

            MifosRadioButtonGroup(
                options = listOf("Telugu", "English", "Hindi", "Spanish"),
                selectedOption = selectedLanguage,
                onSelectionChange = { selectedLanguage = it },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.size(DesignToken.spacing.medium))

            // Payment methods group
            Text(
                text = "Payment Methods (with Error)",
                style = MifosTypography.headlineMedium,
                modifier = Modifier.padding(bottom = DesignToken.spacing.small),
            )

            MifosRadioButtonGroup(
                options = listOf("Credit Card", "Debit Card", "Net Banking", "UPI"),
                selectedOption = selectedPayment,
                onSelectionChange = { selectedPayment = it },
                modifier = Modifier.fillMaxWidth(),
                isError = selectedPayment == null,
            )
        }
    }
}

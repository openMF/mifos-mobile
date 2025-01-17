/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

@Composable
fun nonLetterColorVisualTransformation(): VisualTransformation {
    val digitColor = MaterialTheme.colorScheme.primary
    val specialCharacterColor = MaterialTheme.colorScheme.error
    return remember(digitColor, specialCharacterColor) {
        NonLetterColorVisualTransformation(
            digitColor = digitColor,
            specialCharacterColor = specialCharacterColor,
        )
    }
}

private class NonLetterColorVisualTransformation(
    private val digitColor: Color,
    private val specialCharacterColor: Color,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText =
        TransformedText(
            buildTransformedAnnotatedString(text.toString()),
            OffsetMapping.Identity,
        )

    private fun buildTransformedAnnotatedString(text: String): AnnotatedString {
        val builder = AnnotatedString.Builder()
        text.toCharArray().forEach { char ->
            when {
                char.isDigit() -> builder.withStyle(SpanStyle(color = digitColor)) { append(char) }

                !char.isLetter() -> {
                    builder.withStyle(SpanStyle(color = specialCharacterColor)) { append(char) }
                }

                else -> builder.append(char)
            }
        }
        return builder.toAnnotatedString()
    }
}

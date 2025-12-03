/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun ClickableOrText(
    modifier: Modifier = Modifier,
    value: String,
    textStyle: TextStyle? = null,
    onClick: (() -> Unit)? = null,
) {
    Box(modifier) {
        if (onClick != null) {
            val annotatedString =
                buildAnnotatedString {
                    withStyle(
                        style =
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                            ),
                    ) {
                        append(value)
                    }
                }
            Text(
                modifier =
                    Modifier.clickable(
                        onClick = onClick,
                        indication = null,
                        interactionSource = null,
                    ),
                text = annotatedString,
                style = textStyle ?: LocalTextStyle.current,
            )
        } else {
            Text(
                text = value,
                style = textStyle ?: LocalTextStyle.current,
            )
        }
    }
}

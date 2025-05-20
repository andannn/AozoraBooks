/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import me.andannn.aozora.core.data.common.ReaderTheme

fun ReaderTheme.getBackgroundColor(colorScheme: ColorScheme): Color =
    when (this) {
        ReaderTheme.MONOCHROME -> Grey90
        ReaderTheme.DYNAMIC -> colorScheme.surfaceContainerHigh
        ReaderTheme.PAPER -> Paper80
        ReaderTheme.GREEN_EYE_CARE -> GreenBackground
    }

fun ReaderTheme.getTextColor(colorScheme: ColorScheme): Color =
    when (this) {
        ReaderTheme.MONOCHROME -> Grey10
        ReaderTheme.DYNAMIC -> colorScheme.onSurfaceVariant
        ReaderTheme.PAPER -> Ink
        ReaderTheme.GREEN_EYE_CARE -> GreenText
    }

val RandomColor: Color
    get() {
        return Color(
            alpha = 255 / 6,
            red = (0..255).random(),
            green = (0..255).random(),
            blue = (0..255).random(),
        )
    }

package me.andannn.aozora.ui.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import me.andannn.aozora.core.data.common.ReaderTheme

fun ReaderTheme.getBackgroundColor(colorScheme: ColorScheme): Color {
    return when (this) {
        ReaderTheme.MONOCHROME -> Grey90
        ReaderTheme.DYNAMIC -> colorScheme.surfaceContainerHighest
        ReaderTheme.PAPER -> Paper80
        ReaderTheme.GREEN_EYE_CARE -> GreenBackground
    }
}

fun ReaderTheme.getTextColor(colorScheme: ColorScheme): Color = when (this) {
    ReaderTheme.MONOCHROME -> Grey10
    ReaderTheme.DYNAMIC -> colorScheme.onSurfaceVariant
    ReaderTheme.PAPER -> Ink
    ReaderTheme.GREEN_EYE_CARE -> GreenText
}
package me.andannn.aosora.core.common

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import me.andannn.aosora.ui.theme.GreenBackground
import me.andannn.aosora.ui.theme.GreenText
import me.andannn.aosora.ui.theme.Grey10
import me.andannn.aosora.ui.theme.Grey90
import me.andannn.aosora.ui.theme.Ink
import me.andannn.aosora.ui.theme.Paper80

/**
 * The theme of the reader.
 */
enum class ReaderTheme {
    /**
     * Black and white theme.
     */
    MONOCHROME,

    /**
     * Follow system theme.
     */
    DYNAMIC,

    /**
     * Sepia-toned theme that mimics paper.
     */
    PAPER,

    GREEN_EYE_CARE,
    ;
}

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
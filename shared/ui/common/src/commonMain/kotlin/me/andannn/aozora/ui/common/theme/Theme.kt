/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val LightAozoraColorScheme =
    lightColorScheme(
        primary = Color(0xFF3B5BA5),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD8E2FF),
        onPrimaryContainer = Color(0xFF001B3F),
        inversePrimary = Color(0xFFACC7FF),
        secondary = Color(0xFF56607D),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDDE1F9),
        onSecondaryContainer = Color(0xFF131B34),
        background = Color(0xFFFCFCFF),
        onBackground = Color(0xFF1A1C1E),
        surface = Color(0xFFFCFCFF),
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFE1E2EC),
        onSurfaceVariant = Color(0xFF44464F),
        inverseSurface = Color(0xFF2E313B),
        inverseOnSurface = Color(0xFFE3E2E6),
        outline = Color(0xFF767680),
        outlineVariant = Color(0xFFC6C6D0),
        scrim = Color(0x66000000),
        surfaceContainer = Color(0xFFF1F2F9),
        surfaceContainerHigh = Color(0xFFE7E9F3),
        surfaceContainerHighest = Color(0xFFDEE1ED),
        surfaceContainerLow = Color(0xFFF6F7FC),
        surfaceContainerLowest = Color(0xFFFDFDFF),
        surfaceDim = Color(0xFFE3E4EA),
    )

internal val DarkAozoraColorScheme =
    darkColorScheme(
        primary = Color(0xFFACC7FF),
        onPrimary = Color(0xFF002F65),
        primaryContainer = Color(0xFF214487),
        onPrimaryContainer = Color(0xFFD8E2FF),
        inversePrimary = Color(0xFF3B5BA5),
        secondary = Color(0xFFBCC5DC),
        onSecondary = Color(0xFF273043),
        secondaryContainer = Color(0xFF3E475C),
        onSecondaryContainer = Color(0xFFDDE1F9),
        background = Color(0xFF12131A),
        onBackground = Color(0xFFE3E2E6),
        surface = Color(0xFF12131A),
        onSurface = Color(0xFFE3E2E6),
        surfaceVariant = Color(0xFF2B2F38),
        onSurfaceVariant = Color(0xFFCAC5DC),
        inverseSurface = Color(0xFFFCFCFF),
        inverseOnSurface = Color(0xFF1A1C1E),
        outline = Color(0xFF8F9099),
        outlineVariant = Color(0xFF4C4E57),
        scrim = Color(0x66000000),
        surfaceContainer = Color(0xFF1E2027),
        surfaceContainerHigh = Color(0xFF262931),
        surfaceContainerHighest = Color(0xFF2E313B),
        surfaceContainerLow = Color(0xFF1A1C22),
        surfaceContainerLowest = Color(0xFF141518),
        surfaceDim = Color(0xFF1E1F23),
    )

@Composable
fun AozoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = getColorScheme(darkTheme, dynamicColor)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DefaultTypography,
        content = content,
    )
}

@Composable
expect fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
): ColorScheme

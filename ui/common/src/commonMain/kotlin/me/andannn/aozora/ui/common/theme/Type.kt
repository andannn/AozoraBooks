/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import aosora.ui.common.generated.resources.Res
import aosora.ui.common.generated.resources.noto_serif_jp_regular
import me.andannn.aozora.core.data.common.FontType
import org.jetbrains.compose.resources.Font

// Set of Material typography styles to start with
val DefaultTypography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
     */
    )

@Composable
fun getFontFamilyByType(type: FontType): FontFamily =
    when (type) {
        FontType.NOTO_SANS -> FontFamily.Default
        FontType.NOTO_SERIF -> NotoSerifJpFontFamily
    }

val NotoSerifJpFontFamily
    @Composable get() =
        FontFamily(
            Font(
                resource = Res.font.noto_serif_jp_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal,
            ),
        )

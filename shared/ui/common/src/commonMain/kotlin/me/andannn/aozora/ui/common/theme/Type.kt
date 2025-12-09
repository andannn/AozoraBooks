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
import aosora.shared.ui.common.generated.resources.Res
import aosora.shared.ui.common.generated.resources.hachi_maru_pop_regular
import aosora.shared.ui.common.generated.resources.kaisei_decol_medium
import aosora.shared.ui.common.generated.resources.noto_serif_jp_regular
import aosora.shared.ui.common.generated.resources.yuji_mai_regular
import me.andannn.aozora.core.domain.model.FontType
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
        FontType.HACHI_MARU_POP -> HachiMaruPopFontFamily
        FontType.KAISEI_DECOL_MEDIUM -> KaiseiDecolMediumFontFamily
        FontType.YUJI_MAI -> YujiMaiFontFamily
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

val HachiMaruPopFontFamily
    @Composable get() =
        FontFamily(
            Font(
                resource = Res.font.hachi_maru_pop_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal,
            ),
        )

val KaiseiDecolMediumFontFamily
    @Composable get() =
        FontFamily(
            Font(
                resource = Res.font.kaisei_decol_medium,
                weight = FontWeight.Normal,
                style = FontStyle.Normal,
            ),
        )

val YujiMaiFontFamily
    @Composable get() =
        FontFamily(
            Font(
                resource = Res.font.yuji_mai_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal,
            ),
        )

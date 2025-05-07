package me.andannn.aozora.core.datastore.model

import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin

data class UserSettingPref(
    val topMargin: TopMargin,
    val fontSizeLevel: FontSizeLevel,
    val lineSpacing: LineSpacing,
    val readerTheme: ReaderTheme,
    val fontType: FontType,
)

fun TopMargin.toTopMarginValue() =
    when (this) {
        TopMargin.SMALL -> TopMarginValues.TOP_MARGIN_SMALL_VALUE
        TopMargin.MEDIUM -> TopMarginValues.TOP_MARGIN_MEDIUM_VALUE
        TopMargin.LARGE -> TopMarginValues.TOP_MARGIN_LARGE_VALUE
    }

fun FontSizeLevel.toFontSizeLevelValue() =
    when (this) {
        FontSizeLevel.LEVEL_1 -> FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_1_VALUE
        FontSizeLevel.LEVEL_2 -> FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_2_VALUE
        FontSizeLevel.LEVEL_3 -> FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_3_VALUE
        FontSizeLevel.LEVEL_4 -> FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_4_VALUE
        FontSizeLevel.LEVEL_5 -> FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_5_VALUE
        FontSizeLevel.LEVEL_6 -> FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_6_VALUE
    }

fun LineSpacing.toLineSpacingValue() =
    when (this) {
        LineSpacing.SMALL -> LineSpacingValues.LINE_SPACING_SMALL_VALUE
        LineSpacing.MEDIUM -> LineSpacingValues.LINE_SPACING_MEDIUM_VALUE
        LineSpacing.LARGE -> LineSpacingValues.LINE_SPACING_LARGE_VALUE
    }

fun ReaderTheme.toReaderThemeValue() =
    when (this) {
        ReaderTheme.MONOCHROME -> ReaderThemeValues.READER_THEME_MONOCHROME_VALUE
        ReaderTheme.DYNAMIC -> ReaderThemeValues.READER_THEME_DYNAMIC_VALUE
        ReaderTheme.PAPER -> ReaderThemeValues.READER_THEME_PAPER_VALUE
        ReaderTheme.GREEN_EYE_CARE -> ReaderThemeValues.READER_THEME_GREEN_EYE_CARE_VALUE
    }

fun FontType.toFontTypeValue() =
    when (this) {
        FontType.NOTO_SANS -> FontTypeValues.FONT_TYPE_NOTO_SANS_VALUE
        FontType.NOTO_SERIF -> FontTypeValues.FONT_TYPE_NOTO_SERIF_VALUE
    }

fun parseTopMargin(value: Int): TopMargin =
    when (value) {
        TopMarginValues.TOP_MARGIN_SMALL_VALUE -> TopMargin.SMALL
        TopMarginValues.TOP_MARGIN_MEDIUM_VALUE -> TopMargin.MEDIUM
        TopMarginValues.TOP_MARGIN_LARGE_VALUE -> TopMargin.LARGE
        else -> throw IllegalArgumentException("Unknown top margin value: $value")
    }

fun parseFontSizeLevel(value: Int): FontSizeLevel =
    when (value) {
        FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_1_VALUE -> FontSizeLevel.LEVEL_1
        FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_2_VALUE -> FontSizeLevel.LEVEL_2
        FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_3_VALUE -> FontSizeLevel.LEVEL_3
        FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_4_VALUE -> FontSizeLevel.LEVEL_4
        FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_5_VALUE -> FontSizeLevel.LEVEL_5
        FontSizeLevelValues.FONT_SIZE_LEVEL_LEVEL_6_VALUE -> FontSizeLevel.LEVEL_6
        else -> throw IllegalArgumentException("Unknown font size level value: $value")
    }

fun parseLineSpacing(value: Int): LineSpacing =
    when (value) {
        LineSpacingValues.LINE_SPACING_SMALL_VALUE -> LineSpacing.SMALL
        LineSpacingValues.LINE_SPACING_MEDIUM_VALUE -> LineSpacing.MEDIUM
        LineSpacingValues.LINE_SPACING_LARGE_VALUE -> LineSpacing.LARGE
        else -> throw IllegalArgumentException("Unknown line spacing value: $value")
    }

fun parseReaderTheme(value: Int): ReaderTheme =
    when (value) {
        ReaderThemeValues.READER_THEME_MONOCHROME_VALUE -> ReaderTheme.MONOCHROME
        ReaderThemeValues.READER_THEME_DYNAMIC_VALUE -> ReaderTheme.DYNAMIC
        ReaderThemeValues.READER_THEME_PAPER_VALUE -> ReaderTheme.PAPER
        ReaderThemeValues.READER_THEME_GREEN_EYE_CARE_VALUE -> ReaderTheme.GREEN_EYE_CARE
        else -> throw IllegalArgumentException("Unknown reader theme value: $value")
    }

fun parseFontType(value: Int): FontType =
    when (value) {
        FontTypeValues.FONT_TYPE_NOTO_SANS_VALUE -> FontType.NOTO_SANS
        FontTypeValues.FONT_TYPE_NOTO_SERIF_VALUE -> FontType.NOTO_SERIF
        else -> throw IllegalArgumentException("Unknown font type value: $value")
    }

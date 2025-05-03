package me.andannn.aozora.core.data

import kotlinx.coroutines.flow.StateFlow
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin

/**
 * Repository for user setting.
 */
interface UserSettingRepository {
    /**
     * Get the font size level.
     */
    fun getFontSizeLevel(): StateFlow<FontSizeLevel>

    /**
     * Get the font type.
     */
    fun getFontFontType(): StateFlow<FontType>

    /**
     * Get the top margin.
     */
    fun getTopMargin(): StateFlow<TopMargin>

    /**
     * Get the line spacing.
     */
    fun getLineSpacing(): StateFlow<LineSpacing>

    /**
     * Get the reader theme.
     */
    fun getReaderTheme(): StateFlow<ReaderTheme>

    /**
     * Set the font size level.
     */
    suspend fun setFontSizeLevel(fontSizeLevel: FontSizeLevel)

    /**
     * Set the font type.
     */
    suspend fun setFontType(fontType: FontType)

    /**
     * Set the top margin.
     */
    suspend fun setTopMargin(topMargin: TopMargin)

    /**
     * Set the line spacing.
     */
    suspend fun setLineSpacing(lineSpacing: LineSpacing)

    /**
     * Set the reader theme.
     */
    suspend fun setReaderTheme(readerTheme: ReaderTheme)
}

package me.andannn.aozora.core.data.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin

internal class UserDataRepositoryImpl : UserDataRepository {
    private val fontSizeLevelFlow = MutableStateFlow(FontSizeLevel.LEVEL_4)

    private val fontTypeFlow = MutableStateFlow(FontType.DEFAULT)

    private val topMarginFlow = MutableStateFlow(TopMargin.MEDIUM)

    private val lineSpacingFlow = MutableStateFlow(LineSpacing.MEDIUM)

    private val readerThemeFlow = MutableStateFlow(ReaderTheme.DYNAMIC)

    private val progressFlow = MutableStateFlow(mutableMapOf<String, Int>())

    override fun getFontSizeLevel(): StateFlow<FontSizeLevel> = fontSizeLevelFlow

    override fun getFontFontType(): StateFlow<FontType> = fontTypeFlow

    override fun getTopMargin(): StateFlow<TopMargin> = topMarginFlow

    override fun getLineSpacing(): StateFlow<LineSpacing> = lineSpacingFlow

    override fun getReaderTheme(): StateFlow<ReaderTheme> = readerThemeFlow

    override suspend fun setFontSizeLevel(fontSizeLevel: FontSizeLevel) {
        fontSizeLevelFlow.value = fontSizeLevel
    }

    override suspend fun setFontType(fontType: FontType) {
        fontTypeFlow.value = fontType
    }

    override suspend fun setTopMargin(topMargin: TopMargin) {
        topMarginFlow.value = topMargin
    }

    override suspend fun setLineSpacing(lineSpacing: LineSpacing) {
        lineSpacingFlow.value = lineSpacing
    }

    override suspend fun setReaderTheme(readerTheme: ReaderTheme) {
        readerThemeFlow.value = readerTheme
    }

    override suspend fun setProgressOfBook(
        bookCardId: String,
        blockIndex: Int?,
    ) {
        progressFlow.update {
            it[bookCardId] = blockIndex ?: 0
            it
        }
    }

    override fun getProgressFlow(bookCardId: String): Flow<Int?> =
        progressFlow.map {
            it[bookCardId]
        }

    override suspend fun getProgress(bookCardId: String): Int? = progressFlow.value[bookCardId]
}

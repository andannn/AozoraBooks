/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin
import me.andannn.aozora.core.datastore.UserSettingPreferences

internal class UserDataRepositoryImpl(
    private val preferences: UserSettingPreferences,
) : UserDataRepository {
    private val progressFlow = MutableStateFlow(mutableMapOf<String, Int>())

    override fun getFontSizeLevel(): Flow<FontSizeLevel> =
        preferences.userData.map {
            it.fontSizeLevel
        }

    override fun getFontFontType(): Flow<FontType> =
        preferences.userData.map {
            it.fontType
        }

    override fun getTopMargin(): Flow<TopMargin> =
        preferences.userData.map {
            it.topMargin
        }

    override fun getLineSpacing(): Flow<LineSpacing> =
        preferences.userData.map {
            it.lineSpacing
        }

    override fun getReaderTheme(): Flow<ReaderTheme> =
        preferences.userData.map {
            it.readerTheme
        }

    override suspend fun setFontSizeLevel(fontSizeLevel: FontSizeLevel) {
        preferences.setFontSizeLevel(fontSizeLevel)
    }

    override suspend fun setFontType(fontType: FontType) {
        preferences.setFontType(fontType)
    }

    override suspend fun setTopMargin(topMargin: TopMargin) {
        preferences.setTopMargin(topMargin)
    }

    override suspend fun setLineSpacing(lineSpacing: LineSpacing) {
        preferences.setLineSpacing(lineSpacing)
    }

    override suspend fun setReaderTheme(readerTheme: ReaderTheme) {
        preferences.setReaderTheme(readerTheme)
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

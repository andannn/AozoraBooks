/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserSettingPreferencesTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private val datastoreScope = testScope.backgroundScope

    private lateinit var preferences: UserSettingPreferences

    private val randomInt
        get() = (0..1000).random()
    private val dataStoreFileName = "test.preferences_pb"

    @BeforeTest
    fun setUp() {
        val dataStore =
            PreferenceDataStoreFactory.createWithPath(
                scope = datastoreScope,
            ) {
                "${FileSystem.SYSTEM_TEMPORARY_DIRECTORY}/${randomInt}_$dataStoreFileName".toPath()
            }
        preferences = UserSettingPreferences(dataStore)
    }

    @Test
    fun setTopMarginTest() =
        testScope.runTest {
            assertEquals(TopMargin.DEFAULT, preferences.userData.first().topMargin)

            preferences.setTopMargin(TopMargin.SMALL)
            assertEquals(TopMargin.SMALL, preferences.userData.first().topMargin)

            preferences.setTopMargin(TopMargin.LARGE)
            assertEquals(TopMargin.LARGE, preferences.userData.first().topMargin)

            preferences.setTopMargin(TopMargin.MEDIUM)
            assertEquals(TopMargin.MEDIUM, preferences.userData.first().topMargin)
        }

    @Test
    fun setFontSizeTest() =
        testScope.runTest {
            assertEquals(FontSizeLevel.DEFAULT, preferences.userData.first().fontSizeLevel)

            preferences.setFontSizeLevel(FontSizeLevel.LEVEL_1)
            assertEquals(FontSizeLevel.LEVEL_1, preferences.userData.first().fontSizeLevel)

            preferences.setFontSizeLevel(FontSizeLevel.LEVEL_2)
            assertEquals(FontSizeLevel.LEVEL_2, preferences.userData.first().fontSizeLevel)

            preferences.setFontSizeLevel(FontSizeLevel.LEVEL_3)
            assertEquals(FontSizeLevel.LEVEL_3, preferences.userData.first().fontSizeLevel)

            preferences.setFontSizeLevel(FontSizeLevel.LEVEL_4)
            assertEquals(FontSizeLevel.LEVEL_4, preferences.userData.first().fontSizeLevel)

            preferences.setFontSizeLevel(FontSizeLevel.LEVEL_5)
            assertEquals(FontSizeLevel.LEVEL_5, preferences.userData.first().fontSizeLevel)

            preferences.setFontSizeLevel(FontSizeLevel.LEVEL_6)
            assertEquals(FontSizeLevel.LEVEL_6, preferences.userData.first().fontSizeLevel)
        }

    @Test
    fun setTopMargin() =
        testScope.runTest {
            assertEquals(TopMargin.DEFAULT, preferences.userData.first().topMargin)

            preferences.setTopMargin(TopMargin.SMALL)
            assertEquals(TopMargin.SMALL, preferences.userData.first().topMargin)

            preferences.setTopMargin(TopMargin.LARGE)
            assertEquals(TopMargin.LARGE, preferences.userData.first().topMargin)

            preferences.setTopMargin(TopMargin.MEDIUM)
            assertEquals(TopMargin.MEDIUM, preferences.userData.first().topMargin)
        }

    @Test
    fun setTheme() =
        testScope.runTest {
            assertEquals(ReaderTheme.DEFAULT, preferences.userData.first().readerTheme)

            preferences.setReaderTheme(ReaderTheme.MONOCHROME)
            assertEquals(ReaderTheme.MONOCHROME, preferences.userData.first().readerTheme)

            preferences.setReaderTheme(ReaderTheme.DYNAMIC)
            assertEquals(ReaderTheme.DYNAMIC, preferences.userData.first().readerTheme)

            preferences.setReaderTheme(ReaderTheme.PAPER)
            assertEquals(ReaderTheme.PAPER, preferences.userData.first().readerTheme)

            preferences.setReaderTheme(ReaderTheme.GREEN_EYE_CARE)
            assertEquals(ReaderTheme.GREEN_EYE_CARE, preferences.userData.first().readerTheme)
        }

    @Test
    fun setFontType() =
        testScope.runTest {
            assertEquals(FontType.DEFAULT, preferences.userData.first().fontType)

            preferences.setFontType(FontType.NOTO_SANS)
            assertEquals(FontType.NOTO_SANS, preferences.userData.first().fontType)

            preferences.setFontType(FontType.NOTO_SERIF)
            assertEquals(FontType.NOTO_SERIF, preferences.userData.first().fontType)
        }
}

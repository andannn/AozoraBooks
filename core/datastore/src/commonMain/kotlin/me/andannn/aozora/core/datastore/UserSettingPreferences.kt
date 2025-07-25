/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.andannn.aozora.core.datastore.model.PreferencesKeyName
import me.andannn.aozora.core.datastore.model.UserSettingPref
import me.andannn.aozora.core.datastore.model.parseFontSizeLevel
import me.andannn.aozora.core.datastore.model.parseFontType
import me.andannn.aozora.core.datastore.model.parseLineSpacing
import me.andannn.aozora.core.datastore.model.parseReaderTheme
import me.andannn.aozora.core.datastore.model.parseTopMargin
import me.andannn.aozora.core.datastore.model.toFontSizeLevelValue
import me.andannn.aozora.core.datastore.model.toFontTypeValue
import me.andannn.aozora.core.datastore.model.toLineSpacingValue
import me.andannn.aozora.core.datastore.model.toReaderThemeValue
import me.andannn.aozora.core.datastore.model.toTopMarginValue
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin

class UserSettingPreferences(
    private val preferences: DataStore<Preferences>,
) {
    val userData: Flow<UserSettingPref> =
        preferences.data
            .map { preferences ->
                UserSettingPref(
                    topMargin =
                        preferences[intPreferencesKey(PreferencesKeyName.TOP_MARGIN_KEY_NAME)]?.let {
                            parseTopMargin(it)
                        } ?: TopMargin.DEFAULT,
                    fontSizeLevel =
                        preferences[intPreferencesKey(PreferencesKeyName.FONT_SIZE_LEVEL_KEY_NAME)]?.let {
                            parseFontSizeLevel(it)
                        } ?: FontSizeLevel.DEFAULT,
                    lineSpacing =
                        preferences[intPreferencesKey(PreferencesKeyName.LINE_SPACING_KEY_NAME)]?.let {
                            parseLineSpacing(it)
                        } ?: LineSpacing.DEFAULT,
                    readerTheme =
                        preferences[intPreferencesKey(PreferencesKeyName.READER_THEME_KEY_NAME)]?.let {
                            parseReaderTheme(it)
                        } ?: ReaderTheme.DEFAULT,
                    fontType =
                        preferences[intPreferencesKey(PreferencesKeyName.FONT_TYPE_KEY_NAME)]?.let {
                            parseFontType(it)
                        } ?: FontType.DEFAULT,
                    lastSuccessfulSyncTime =
                        preferences[stringPreferencesKey(PreferencesKeyName.LAST_SUCCESSFUL_SYNC_TIME_KEY_NAME)],
                    ndcTableMigrated =
                        preferences[booleanPreferencesKey(PreferencesKeyName.NDC_TABLE_MIGRATED_KEY_NAME)]
                            ?: false,
                )
            }

    suspend fun setTopMargin(topMargin: TopMargin) {
        preferences.edit { preferences ->
            preferences[intPreferencesKey(PreferencesKeyName.TOP_MARGIN_KEY_NAME)] =
                topMargin.toTopMarginValue()
        }
    }

    suspend fun setFontSizeLevel(fontSizeLevel: FontSizeLevel) {
        preferences.edit { preferences ->
            preferences[intPreferencesKey(PreferencesKeyName.FONT_SIZE_LEVEL_KEY_NAME)] =
                fontSizeLevel.toFontSizeLevelValue()
        }
    }

    suspend fun setLineSpacing(lineSpacing: LineSpacing) {
        preferences.edit { preferences ->
            preferences[intPreferencesKey(PreferencesKeyName.LINE_SPACING_KEY_NAME)] =
                lineSpacing.toLineSpacingValue()
        }
    }

    suspend fun setReaderTheme(readerTheme: ReaderTheme) {
        preferences.edit { preferences ->
            preferences[intPreferencesKey(PreferencesKeyName.READER_THEME_KEY_NAME)] =
                readerTheme.toReaderThemeValue()
        }
    }

    suspend fun setFontType(fontType: FontType) {
        preferences.edit { preferences ->
            preferences[intPreferencesKey(PreferencesKeyName.FONT_TYPE_KEY_NAME)] =
                fontType.toFontTypeValue()
        }
    }

    suspend fun setLastSuccessfulSyncTime(time: String) {
        preferences.edit { preferences ->
            preferences[stringPreferencesKey(PreferencesKeyName.LAST_SUCCESSFUL_SYNC_TIME_KEY_NAME)] =
                time
        }
    }

    suspend fun setNdcTableMigrated(migrated: Boolean) {
        preferences.edit { preferences ->
            preferences[booleanPreferencesKey(PreferencesKeyName.NDC_TABLE_MIGRATED_KEY_NAME)] =
                migrated
        }
    }
}

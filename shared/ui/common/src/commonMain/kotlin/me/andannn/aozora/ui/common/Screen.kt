/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Screen : NavKey

@Serializable
data object HomeScreen : Screen

@Serializable
data object LibraryNestedScreen : Screen

@Serializable
data object SearchNestedScreen : Screen

@Serializable
data object LicenseScreen : Screen

@Serializable
data object AboutScreen : Screen

@Serializable
data class ReaderScreen(
    val cardId: String,
    val authorId: String,
) : Screen

@Serializable
data class IndexPageScreen(
    val kana: String,
) : Screen

@Serializable
data class AuthorPagesScreen(
    val code: String,
) : Screen

@Serializable
data class BookCardScreen(
    val bookCardId: String,
    val groupId: String,
) : Screen

@Serializable
data class AuthorScreen(
    val authorId: String,
) : Screen

@Serializable
data class SearchInputScreen(
    val initialParam: String?,
) : Screen

@Serializable
data class SearchResultScreen(
    val query: String,
) : Screen

@Serializable
data class NdcContentScreen(
    val ndc: String,
) : Screen

fun buildSavedStateConfiguration() =
    SavedStateConfiguration {
        serializersModule =
            SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(HomeScreen::class, HomeScreen.serializer())
                    subclass(LibraryNestedScreen::class, LibraryNestedScreen.serializer())
                    subclass(SearchNestedScreen::class, SearchNestedScreen.serializer())
                    subclass(LicenseScreen::class, LicenseScreen.serializer())
                    subclass(AboutScreen::class, AboutScreen.serializer())
                    subclass(ReaderScreen::class, ReaderScreen.serializer())
                    subclass(IndexPageScreen::class, IndexPageScreen.serializer())
                    subclass(AuthorPagesScreen::class, AuthorPagesScreen.serializer())
                    subclass(BookCardScreen::class, BookCardScreen.serializer())
                    subclass(AuthorScreen::class, AuthorScreen.serializer())
                    subclass(SearchInputScreen::class, SearchInputScreen.serializer())
                    subclass(SearchResultScreen::class, SearchResultScreen.serializer())
                    subclass(NdcContentScreen::class, NdcContentScreen.serializer())
                }
            }
    }

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.common.screens

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.andannn.aozora.ui.feature.about.About
import me.andannn.aozora.ui.feature.about.AboutState
import me.andannn.aozora.ui.feature.about.rememberAboutPresenter
import me.andannn.aozora.ui.feature.author.Author
import me.andannn.aozora.ui.feature.author.AuthorState
import me.andannn.aozora.ui.feature.author.rememberAuthorPresenter
import me.andannn.aozora.ui.feature.authorpages.AuthorPages
import me.andannn.aozora.ui.feature.authorpages.AuthorPagesState
import me.andannn.aozora.ui.feature.authorpages.rememberAuthorPagesPresenter
import me.andannn.aozora.ui.feature.bookcard.BookCard
import me.andannn.aozora.ui.feature.bookcard.BookCardState
import me.andannn.aozora.ui.feature.bookcard.rememberBookCardPresenter
import me.andannn.aozora.ui.feature.home.Home
import me.andannn.aozora.ui.feature.home.HomeState
import me.andannn.aozora.ui.feature.home.library.Library
import me.andannn.aozora.ui.feature.home.library.LibraryState
import me.andannn.aozora.ui.feature.home.library.rememberLibraryPresenter
import me.andannn.aozora.ui.feature.home.rememberHomePresenter
import me.andannn.aozora.ui.feature.home.search.Search
import me.andannn.aozora.ui.feature.home.search.SearchState
import me.andannn.aozora.ui.feature.home.search.rememberSearchPresenter
import me.andannn.aozora.ui.feature.home.searchinput.SearchInput
import me.andannn.aozora.ui.feature.home.searchinput.SearchInputState
import me.andannn.aozora.ui.feature.home.searchinput.rememberSearchInputPresenter
import me.andannn.aozora.ui.feature.home.searchresult.SearchResult
import me.andannn.aozora.ui.feature.home.searchresult.SearchResultState
import me.andannn.aozora.ui.feature.home.searchresult.rememberSearchResultPresenter
import me.andannn.aozora.ui.feature.indexpages.IndexPages
import me.andannn.aozora.ui.feature.indexpages.IndexPagesState
import me.andannn.aozora.ui.feature.indexpages.rememberIndexPagesPresenter
import me.andannn.aozora.ui.feature.license.License
import me.andannn.aozora.ui.feature.license.LicenseState
import me.andannn.aozora.ui.feature.license.rememberLicensePresenter
import me.andannn.aozora.ui.feature.reader.Reader
import me.andannn.aozora.ui.feature.reader.ReaderState
import me.andannn.aozora.ui.feature.reader.rememberReaderPresenter

object RouteUiFactory : Ui.Factory {
    override fun create(
        screen: Screen,
        context: CircuitContext,
    ): Ui<*>? =
        when (screen) {
            is HomeScreen ->
                ui<HomeState> { state, modifier ->
                    Home(state, modifier)
                }

            is ReaderScreen ->
                ui<ReaderState> { state, modifier ->
                    Reader(state, modifier)
                }

            is IndexPageScreen -> {
                ui<IndexPagesState> { state, modifier ->
                    IndexPages(state, modifier)
                }
            }

            is BookCardScreen -> {
                ui<BookCardState> { state, modifier ->
                    BookCard(state, modifier)
                }
            }

            is LibraryNestedScreen -> {
                ui<LibraryState> { state, modifier ->
                    Library(state, modifier)
                }
            }

            is SearchNestedScreen -> {
                ui<SearchState> { state, modifier ->
                    Search(state, modifier)
                }
            }

            is LicenseScreen ->
                ui<LicenseState> { state, modifier ->
                    License(state, modifier)
                }

            is AboutScreen ->
                ui<AboutState> { state, modifier ->
                    About(state, modifier)
                }

            is AuthorPagesScreen -> {
                ui<AuthorPagesState> { state, modifier ->
                    AuthorPages(state, modifier)
                }
            }

            is AuthorScreen -> {
                ui<AuthorState> { state, modifier ->
                    Author(state, modifier)
                }
            }

            is SearchInputScreen -> {
                ui<SearchInputState> { state, modifier ->
                    SearchInput(state, modifier)
                }
            }

            is SearchResultScreen -> {
                ui<SearchResultState> { state, modifier ->
                    SearchResult(state, modifier)
                }
            }

            else -> null
        }
}

object RoutePresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? =
        when (screen) {
            is HomeScreen ->
                presenterOf {
                    rememberHomePresenter().present()
                }

            is ReaderScreen ->
                presenterOf {
                    rememberReaderPresenter(screen.cardId).present()
                }

            is IndexPageScreen -> {
                presenterOf {
                    rememberIndexPagesPresenter(screen.kana).present()
                }
            }

            is BookCardScreen -> {
                presenterOf {
                    rememberBookCardPresenter(
                        groupId = screen.groupId,
                        bookId = screen.bookCardId,
                    ).present()
                }
            }

            is LibraryNestedScreen -> {
                presenterOf {
                    rememberLibraryPresenter(nestedNavigator = navigator).present()
                }
            }

            is SearchNestedScreen -> {
                presenterOf {
                    rememberSearchPresenter().present()
                }
            }

            is LicenseScreen ->
                presenterOf {
                    rememberLicensePresenter().present()
                }

            is AboutScreen ->
                presenterOf {
                    rememberAboutPresenter().present()
                }

            is AuthorPagesScreen -> {
                presenterOf {
                    rememberAuthorPagesPresenter(screen.code).present()
                }
            }

            is AuthorScreen -> {
                presenterOf {
                    rememberAuthorPresenter(screen.authorId).present()
                }
            }

            is SearchInputScreen -> {
                presenterOf {
                    rememberSearchInputPresenter(screen.initialParam).present()
                }
            }

            is SearchResultScreen ->
                presenterOf {
                    rememberSearchResultPresenter(screen.query).present()
                }

            else -> null
        }
}

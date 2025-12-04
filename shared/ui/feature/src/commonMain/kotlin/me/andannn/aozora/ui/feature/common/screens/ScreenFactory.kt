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
import me.andannn.aozora.ui.feature.about.retainAboutPresenter
import me.andannn.aozora.ui.feature.author.Author
import me.andannn.aozora.ui.feature.author.AuthorState
import me.andannn.aozora.ui.feature.author.retainAuthorPresenter
import me.andannn.aozora.ui.feature.authorpages.AuthorPages
import me.andannn.aozora.ui.feature.authorpages.AuthorPagesState
import me.andannn.aozora.ui.feature.authorpages.retainAuthorPagesPresenter
import me.andannn.aozora.ui.feature.bookcard.BookCard
import me.andannn.aozora.ui.feature.bookcard.BookCardState
import me.andannn.aozora.ui.feature.bookcard.retainBookCardPresenter
import me.andannn.aozora.ui.feature.home.Home
import me.andannn.aozora.ui.feature.home.HomeState
import me.andannn.aozora.ui.feature.home.library.Library
import me.andannn.aozora.ui.feature.home.library.LibraryState
import me.andannn.aozora.ui.feature.home.library.retainLibraryPresenter
import me.andannn.aozora.ui.feature.home.retainHomePresenter
import me.andannn.aozora.ui.feature.home.search.Search
import me.andannn.aozora.ui.feature.home.search.SearchState
import me.andannn.aozora.ui.feature.home.search.retainSearchPresenter
import me.andannn.aozora.ui.feature.home.searchinput.SearchInput
import me.andannn.aozora.ui.feature.home.searchinput.SearchInputState
import me.andannn.aozora.ui.feature.home.searchinput.retainSearchInputPresenter
import me.andannn.aozora.ui.feature.home.searchresult.SearchResult
import me.andannn.aozora.ui.feature.home.searchresult.SearchResultState
import me.andannn.aozora.ui.feature.home.searchresult.retainSearchResultPresenter
import me.andannn.aozora.ui.feature.indexpages.IndexPages
import me.andannn.aozora.ui.feature.indexpages.IndexPagesState
import me.andannn.aozora.ui.feature.indexpages.retainIndexPagesPresenter
import me.andannn.aozora.ui.feature.license.License
import me.andannn.aozora.ui.feature.license.LicenseState
import me.andannn.aozora.ui.feature.license.retainLicensePresenter
import me.andannn.aozora.ui.feature.ndc.NdcContent
import me.andannn.aozora.ui.feature.ndc.NdcContentState
import me.andannn.aozora.ui.feature.ndc.retainNdcContentPresenter
import me.andannn.aozora.ui.feature.reader.Reader
import me.andannn.aozora.ui.feature.reader.ReaderState
import me.andannn.aozora.ui.feature.reader.retainReaderPresenter

object RouteUiFactory : Ui.Factory {
    override fun create(
        screen: Screen,
        context: CircuitContext,
    ): Ui<*>? =
        when (screen) {
            is HomeScreen -> {
                ui<HomeState> { state, modifier ->
                    Home(state, modifier)
                }
            }

            is ReaderScreen -> {
                ui<ReaderState> { state, modifier ->
                    Reader(state, modifier)
                }
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

            is LicenseScreen -> {
                ui<LicenseState> { state, modifier ->
                    License(state, modifier)
                }
            }

            is AboutScreen -> {
                ui<AboutState> { state, modifier ->
                    About(state, modifier)
                }
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

            is NdcContentScreen -> {
                ui<NdcContentState> { state, modifier ->
                    NdcContent(state, modifier)
                }
            }

            else -> {
                null
            }
        }
}

object RoutePresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? =
        when (screen) {
            is HomeScreen -> {
                presenterOf {
                    retainHomePresenter().present()
                }
            }

            is ReaderScreen -> {
                presenterOf {
                    retainReaderPresenter(screen.cardId, screen.authorId).present()
                }
            }

            is IndexPageScreen -> {
                presenterOf {
                    retainIndexPagesPresenter(screen.kana).present()
                }
            }

            is BookCardScreen -> {
                presenterOf {
                    retainBookCardPresenter(
                        groupId = screen.groupId,
                        bookId = screen.bookCardId,
                    ).present()
                }
            }

            is LibraryNestedScreen -> {
                presenterOf {
                    retainLibraryPresenter().present()
                }
            }

            is SearchNestedScreen -> {
                presenterOf {
                    retainSearchPresenter().present()
                }
            }

            is LicenseScreen -> {
                presenterOf {
                    retainLicensePresenter().present()
                }
            }

            is AboutScreen -> {
                presenterOf {
                    retainAboutPresenter().present()
                }
            }

            is AuthorPagesScreen -> {
                presenterOf {
                    retainAuthorPagesPresenter(screen.code).present()
                }
            }

            is AuthorScreen -> {
                presenterOf {
                    retainAuthorPresenter(screen.authorId).present()
                }
            }

            is SearchInputScreen -> {
                presenterOf {
                    retainSearchInputPresenter(screen.initialParam).present()
                }
            }

            is SearchResultScreen -> {
                presenterOf {
                    retainSearchResultPresenter(screen.query).present()
                }
            }

            is NdcContentScreen -> {
                presenterOf {
                    retainNdcContentPresenter(screen.ndc).present()
                }
            }

            else -> {
                null
            }
        }
}

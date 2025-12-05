package me.andannn.aozora.app

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import me.andannn.aozora.ui.common.AboutScreen
import me.andannn.aozora.ui.common.AuthorPagesScreen
import me.andannn.aozora.ui.common.AuthorScreen
import me.andannn.aozora.ui.common.BookCardScreen
import me.andannn.aozora.ui.common.HomeScreen
import me.andannn.aozora.ui.common.IndexPageScreen
import me.andannn.aozora.ui.common.LibraryNestedScreen
import me.andannn.aozora.ui.common.LicenseScreen
import me.andannn.aozora.ui.common.NdcContentScreen
import me.andannn.aozora.ui.common.ReaderScreen
import me.andannn.aozora.ui.common.SearchInputScreen
import me.andannn.aozora.ui.common.SearchNestedScreen
import me.andannn.aozora.ui.common.SearchResultScreen
import me.andannn.aozora.ui.feature.about.About
import me.andannn.aozora.ui.feature.author.Author
import me.andannn.aozora.ui.feature.author.pages.AuthorPages
import me.andannn.aozora.ui.feature.bookcard.BookCard
import me.andannn.aozora.ui.feature.index.pages.IndexPages
import me.andannn.aozora.ui.feature.library.Library
import me.andannn.aozora.ui.feature.license.License
import me.andannn.aozora.ui.feature.ndc.NdcContent
import me.andannn.aozora.ui.feature.reader.Reader
import me.andannn.aozora.ui.feature.search.Search
import me.andannn.aozora.ui.feature.search.input.SearchInput
import me.andannn.aozora.ui.feature.search.result.SearchResult

fun aosoraScreenEntryProvider() =
    entryProvider<NavKey> {
        entry<HomeScreen> {
            Home()
        }

        entry<ReaderScreen> {
            Reader(cardId = it.cardId, authorId = it.authorId)
        }

        entry<IndexPageScreen> {
            IndexPages(it.kana)
        }

        entry<BookCardScreen> {
            BookCard(groupId = it.groupId, bookId = it.bookCardId)
        }
        entry<AuthorScreen> {
            Author(it.authorId)
        }

        entry<LibraryNestedScreen> {
            Library()
        }

        entry<SearchNestedScreen> {
            Search()
        }

        entry<LicenseScreen> {
            License()
        }

        entry<AboutScreen> {
            About()
        }

        entry<AuthorPagesScreen> {
            AuthorPages(it.code)
        }

        entry<SearchInputScreen> {
            SearchInput(it.initialParam)
        }

        entry<SearchResultScreen> {
            SearchResult(it.query)
        }

        entry<NdcContentScreen> {
            NdcContent(it.ndc)
        }
    }

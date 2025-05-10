/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service.impl

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.AuthorData
import me.andannn.aozora.core.data.common.BookColumnItem
import me.andannn.aozora.core.data.common.StaffData
import me.andannn.aozora.core.data.common.TitleItem
import me.andannn.aozora.core.service.AozoraService

private const val BASE_URL = "https://www.aozora.gr.jp"

class AozoraServiceImpl(
    val httpClient: HttpClient,
) : AozoraService {
    override suspend fun getPageCountOfKana(kana: String): Int {
        val url = getBookPageQueryUrlBy(kana, 1)
        val responseText = httpClient.get(url).bodyAsText()
        return parsePageCount(responseText)
    }

    override suspend fun getBookListOfKanaByPage(
        kana: String,
        page: Int,
    ): List<BookColumnItem> {
        val url = getBookPageQueryUrlBy(kana, page)
        val responseText = httpClient.get(url).bodyAsText()
        return parseBookListFromOpeningBooks(responseText)
    }

    override suspend fun getBookCard(
        groupId: String,
        cardId: String,
    ): AozoraBookCard {
        val url = getBookCardUrlBy(groupId, cardId)
        val responseText = httpClient.get(url).bodyAsText()
        return parseBookCard(
            url = url,
            cardId = cardId,
            groupId = groupId,
            htmlString = responseText,
        )
    }
}

private fun getBookPageQueryUrlBy(
    kana: String,
    page: Int,
): String = "$BASE_URL/index_pages/sakuhin_${kana}$page.html"

private fun getBookCardUrlBy(
    groupId: String,
    cardId: String,
): String = "$BASE_URL/cards/$groupId/card$cardId.html"

internal fun parseBookCard(
    url: String,
    cardId: String,
    groupId: String,
    htmlString: String,
): AozoraBookCard {
    val html = Ksoup.parse(htmlString)

    val titleElement = html.selectFirst("table[summary=タイトルデータ] > tbody")
    val title = titleElement?.child(0)?.child(1)?.text()
    val titleKana = titleElement?.child(1)?.child(1)?.text()

    val bookDataElement = html.selectFirst("table[summary=作品データ] > tbody")
    val category = bookDataElement?.child(0)?.child(1)?.text()
    val source = bookDataElement?.child(1)?.child(1)?.text()
    val characterType = bookDataElement?.child(2)?.child(1)?.text()

    val staffElement = html.selectFirst("table[summary=工作員データ] > tbody")
    val input = staffElement?.child(0)?.child(1)?.text()
    val proofreading = staffElement?.child(1)?.child(1)?.text()

    val authorDataElement = html.selectFirst("table[summary=作家データ] > tbody")
    val authData = parseAuthorDataElement(authorDataElement!!)

    val downloadElement = html.selectFirst("table[summary=ダウンロードデータ] > tbody")
    val zipFileUrl =
        downloadElement!!
            .children()
            .firstOrNull {
                it.text().contains("テキストファイル")
            }?.selectFirst("a")
            ?.attr("href")
    val htmlFileUrl =
        downloadElement
            .children()
            .firstOrNull {
                it.text().contains("XHTMLファイル")
            }?.selectFirst("a")
            ?.attr("href")

// TODO: make resolve helper function.
    val zipDownloadUrl = zipFileUrl?.let { url.replaceAfterLast("/", "") + it.removePrefix(".") }
    val htmlDownloadUrl = htmlFileUrl?.let { url.replaceAfterLast("/", "") + it.removePrefix(".") }

    return AozoraBookCard(
        id = cardId,
        title = title!!,
        titleKana = titleKana!!,
        category = category!!,
        source = source,
        authorData = authData,
        zipUrl = zipDownloadUrl!!,
        htmlUrl = htmlDownloadUrl!!,
        characterType = characterType,
        staffData =
            StaffData(
                input = input!!,
                proofreading = proofreading!!,
            ),
    )
}

private fun parseAuthorDataElement(element: Element): AuthorData {
    val category = element.child(0).child(1).text()
    val authorElement = element.selectFirst("a")
    val authorName = authorElement?.text()
    val authorUrl = BASE_URL + authorElement!!.attr("href").removePrefix("../..")
    val authorKana = element.child(2).child(1).text()
    val authorRomaji = element.child(3).child(1).text()
    val birth = element.child(4).child(1).text()
    val death = element.child(5).child(1).text()
    val descriptionElement = element.child(6).child(1)
    val description = descriptionElement.childNodes.first().toString()
    val descriptionWikiUrl = descriptionElement.select("a").last()?.attr("href")
    return AuthorData(
        category = category,
        authorName = authorName!!,
        authorUrl = authorUrl,
        authorNameKana = authorKana,
        authorNameRomaji = authorRomaji,
        birth = birth,
        death = death,
        description = description,
        descriptionWikiUrl = descriptionWikiUrl!!,
        authorPageUrl = authorUrl,
    )
}

internal fun parseBookListFromOpeningBooks(htmlString: String): List<BookColumnItem> {
    val html = Ksoup.parse(htmlString)
    val listNodes = html.selectFirst("table.list > tbody") ?: error("")
    val results =
        listNodes.children().asSequence().drop(1).map {
            parseBookItem(it)
        }
    return results.toList()
}

private fun parseBookItem(element: Element): BookColumnItem {
    val index = element.child(0).text()
    val titleItem = parseTitle(element.child(1))
    val characterCategory = element.child(2).text()
    val author = element.child(3).text()
    val translator = element.child(5).text()

    val bookColumnItem =
        BookColumnItem(
            index = index,
            title = titleItem,
            characterCategory = characterCategory,
            author = author,
            translator = translator.takeIf { it.isNotEmpty() },
        )
    return bookColumnItem
}

private fun parseTitle(titleElement: Element): TitleItem {
    titleElement.child(0)
    val title = titleElement.child(0).text()
    val link = BASE_URL + titleElement.child(0).attr("href").removePrefix("..")
    val subTitle =
        titleElement
            .childNodes()
            .lastOrNull { it is TextNode && it.text().isNotBlank() }
            ?.toString()

    return TitleItem(
        title = title,
        subTitle = subTitle,
        link = link,
    )
}

internal fun parsePageCount(responseText: String): Int {
    val html = Ksoup.parse(responseText)
    val lastIndex =
        html
            .selectFirst("center > table > tbody > tr")
            ?.children()
            ?.get(1)
            ?.children()
            ?.last()
            ?: return 1
    val lastIndexNum = lastIndex.text().toIntOrNull() ?: 1
    return lastIndexNum
}

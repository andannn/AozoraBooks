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
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.domain.model.BookColumnItem
import me.andannn.aozora.core.domain.model.TitleItem
import me.andannn.aozora.core.service.AozoraService
import me.andannn.core.util.removePrefixRecursive

private const val BASE_URL = "https://www.aozora.gr.jp"

internal class AozoraServiceImpl(
    private val httpClient: HttpClient,
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

    override suspend fun getBookCardAuthorDataList(
        groupId: String,
        cardId: String,
    ): List<AuthorData> {
        val url = getBookCardUrlBy(groupId, cardId.removePrefixRecursive("0"))
        val responseText = httpClient.get(url).bodyAsText()
        return parseAuthorData(responseText)
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

internal fun parseAuthorData(htmlString: String): List<AuthorData> {
    val html = Ksoup.parse(htmlString)
    val authorDataElements = html.select("table[summary=作家データ] > tbody")
    val authDataList =
        authorDataElements.map {
            parseAuthorDataElement(it)
        }

    return authDataList
}

private fun parseAuthorDataElement(element: Element): AuthorData {
    val category = element.child(0).child(1).text()
    val authorElement = element.selectFirst("a")
    val authorName = authorElement?.text()
    val authorUrl = BASE_URL + authorElement!!.attr("href").removePrefix("../..")
    val childrenTextList =
        element.children().map { it.text().trim() }.filterNot { it.isBlank() }
    val authorKana = subTextOfList(childrenTextList, value = "作家名読み：")
    val authorRomaji = subTextOfList(childrenTextList, value = "ローマ字表記：")
    val birth = subTextOfList(childrenTextList, value = "生年：")
    val death = subTextOfList(childrenTextList, value = "没年：")
    val descriptionElement =
        element
            .children()
            .firstOrNull {
                it.text().contains("人物について：")
            }?.children()
            ?.getOrNull(1)
    val isDescriptionEmpty =
        descriptionElement?.let {
            it.childNodes.firstOrNull()?.let { it is Element && it.tagName() == "a" }
        } == true
    val description =
        if (isDescriptionEmpty) {
            null
        } else {
            descriptionElement
                ?.childNodes
                ?.first()
                ?.toString()
                ?.notBlankOrNull()
        }
    val descriptionWikiUrl = descriptionElement?.select("a")?.last()?.attr("href")
    return AuthorData(
        authorId = parseAuthorIdFromUrl(authorUrl),
        category = category,
        authorName = authorName!!,
        authorUrl = authorUrl,
        authorNameKana = authorKana,
        authorNameRomaji = authorRomaji,
        birth = birth,
        death = death,
        description = description,
        descriptionWikiUrl = descriptionWikiUrl,
    )
}

private fun parseAuthorIdFromUrl(authorUrl: String): String = authorUrl.substringAfterLast("/person").removeSuffix(".html").padStart(6, '0')

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

private fun String.notBlankOrNull() = takeIf { it.isNotBlank() }

private fun subTextOfList(
    list: List<String>,
    value: String,
): String? = list.firstOrNull { it.contains(value) }?.substringAfter(value)

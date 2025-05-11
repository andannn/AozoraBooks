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
    val authorElement = titleElement?.select("a")
    val author = authorElement?.text()
    val authorUrl = BASE_URL + authorElement!!.attr("href").removePrefix("../..")

    val bookDataElement = html.selectFirst("table[summary=作品データ] > tbody")
    val childrenTextList =
        bookDataElement?.children()?.map { it.text().trim() }?.filterNot { it.isBlank() }
            ?: listOf()

    val category = subTextOfList(childrenTextList, value = "分類：")
    val source = subTextOfList(childrenTextList, value = "初出：")
    val characterType = subTextOfList(childrenTextList, value = "文字遣い種別：")

    val staffElement = html.selectFirst("table[summary=工作員データ] > tbody")
    val staffTextList =
        staffElement?.children()?.map { it.text().trim() }?.filterNot { it.isBlank() }
            ?: listOf()
    val input = subTextOfList(staffTextList, value = "入力：")
    val proofreading = subTextOfList(staffTextList, value = "校正：")

    val authorDataElements = html.select("table[summary=作家データ] > tbody")
    val authDataList =
        authorDataElements.map {
            parseAuthorDataElement(it)
        }

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
                it.text().contains("HTMLファイル")
            }?.selectFirst("a")
            ?.attr("href")

// TODO: make resolve helper function.
    val zipDownloadUrl = zipFileUrl?.let { url.replaceAfterLast("/", "") + it.removePrefix(".") }
    val htmlDownloadUrl = htmlFileUrl?.let { url.replaceAfterLast("/", "") + it.removePrefix(".") }

    return AozoraBookCard(
        id = cardId,
        groupId = groupId,
        title = title!!,
        titleKana = titleKana!!,
        author = author,
        authorUrl = authorUrl,
        category = category,
        source = source,
        authorDataList = authDataList,
        zipUrl = zipDownloadUrl!!,
        htmlUrl = htmlDownloadUrl!!,
        characterType = characterType,
        staffData =
            StaffData(
                input = input,
                proofreading = proofreading,
            ),
    )
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

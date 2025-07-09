/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.service.impl

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import me.andannn.aozora.core.domain.model.AuthorData
import me.andannn.aozora.core.service.AozoraService
import me.andannn.core.util.removePrefixRecursive

private const val BASE_URL = "https://www.aozora.gr.jp"

internal class AozoraServiceImpl(
    private val httpClient: HttpClient,
) : AozoraService {
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

private fun String.notBlankOrNull() = takeIf { it.isNotBlank() }

private fun subTextOfList(
    list: List<String>,
    value: String,
): String? = list.firstOrNull { it.contains(value) }?.substringAfter(value)

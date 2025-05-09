package me.andannn.aozora.core.service.impl

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import me.andannn.aozora.core.data.common.BookColumnItem
import me.andannn.aozora.core.data.common.TitleItem
import me.andannn.aozora.core.service.AozoraService

private const val BASE_URL = "https://www.aozora.gr.jp"

class AozoraServiceImpl(
    val httpClient: HttpClient,
) : AozoraService {
    override suspend fun getPageCountOfKana(kana: String): Int = 3

    override suspend fun getBookListOfKanaByPage(
        kana: String,
        page: Int,
    ): List<BookColumnItem> {
        val url = getBookPageQueryUrlBy(kana, page)
        val responseText = httpClient.get(url).bodyAsText()
        return parseBookListFromOpeningBooks(responseText)
    }
}

private fun getBookPageQueryUrlBy(
    kana: String,
    page: Int,
): String = "$BASE_URL/index_pages/sakuhin_${kana}$page.html"

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

    val bookColumnItem =
        BookColumnItem(
            index = index,
            title = titleItem,
            characterCategory = characterCategory,
            author = author,
        )
    return bookColumnItem
}

private fun parseTitle(titleElement: Element): TitleItem {
    titleElement.child(0)
    val title = titleElement.child(0).text()
    val link = BASE_URL + titleElement.child(0).attr("href").removePrefix("..")
    val subTitle =
        titleElement.childNodes().lastOrNull { it is TextNode && it.text().isNotBlank() }.toString()

    return TitleItem(
        title = title,
        subTitle = subTitle,
        link = link,
    )
}

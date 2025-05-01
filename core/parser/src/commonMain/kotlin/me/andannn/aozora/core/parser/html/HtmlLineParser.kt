package me.andannn.aozora.core.parser.html

import com.fleeksoft.ksoup.nodes.TextNode
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.parser.AozoraLineParser
import me.andannn.aozora.core.parser.RawLine

object HtmlLineParser : AozoraLineParser {
    override fun parseLine(line: RawLine): List<AozoraElement> {
        return line
            .content
            .parseAsHtmlNodes()
            .filterNot { it is TextNode && it.text().isEmpty() }
            .parseAsAozoraElements()
    }
}

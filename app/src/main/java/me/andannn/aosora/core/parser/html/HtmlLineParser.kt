package me.andannn.aosora.core.parser.html

import me.andannn.aosora.core.common.model.AozoraElement
import me.andannn.aosora.core.parser.AozoraLineParser

object HtmlLineParser: AozoraLineParser {
    override fun parseLine(line: String): List<AozoraElement> {
        return line.parseAsHtmlNodes().parseAsAozoraElements()
    }
}
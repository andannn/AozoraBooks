/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html

import com.fleeksoft.ksoup.nodes.TextNode
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.parser.AozoraLineParser
import me.andannn.aozora.core.pagesource.parser.RawLine

internal object HtmlLineParser : AozoraLineParser {
    override fun parseLine(line: RawLine): List<AozoraElement> {
        return line
            .content
            .parseAsHtmlNodes()
            .filterNot { it is TextNode && it.text().isEmpty() }
            .parseAsAozoraElements()
    }
}

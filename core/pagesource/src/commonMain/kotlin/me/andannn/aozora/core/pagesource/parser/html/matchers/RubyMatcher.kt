/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher

internal object RubyMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        if (node !is Element) return null
        if (node.tagName() != "ruby") {
            return null
        }
        val text = node.select("rb").text()
        val rt = node.select("rt").text()
        return AozoraElement.Ruby(text, rt)
    }
}

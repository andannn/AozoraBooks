/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher

internal object ImageMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        if (node !is Element) return null
        if (node.tagName() != "img") {
            return null
        }
        val width = node.attr("width").toIntOrNull() ?: return null
        val height = node.attr("height").toIntOrNull() ?: return null
        val src = node.attr("src") ?: return null
        return AozoraElement.Illustration(src, width, height)
    }
}

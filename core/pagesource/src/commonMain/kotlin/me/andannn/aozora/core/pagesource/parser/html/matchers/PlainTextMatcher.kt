/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.parser.html.matchers

import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.pagesource.parser.html.ElementMatcher

internal object PlainTextMatcher : ElementMatcher {
    override fun match(node: Node): AozoraElement? {
        if (node !is TextNode) return null

        return AozoraElement.Text(node.text())
    }
}

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.core.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import io.github.aakira.napier.Napier

fun parseBookSourceAsAnnotatedString(html: String): AnnotatedString {
    val childNodes =
        Ksoup
            .parseBodyFragment(html)
            .body()
            .childNodes()
            .first()
            .childNodes()

    return buildAnnotatedString {
        childNodes
            .asSequence()
            .filterNot { it is TextNode && (it.text().isBlank() || it.text().isEmpty()) }
            .forEach {
                build(it)
            }
    }
}

private fun AnnotatedString.Builder.build(node: Node) {
    when (node) {
        is TextNode -> append(node.text())
        is Element -> {
            when (node.tagName()) {
                "br" -> {
                    append("\n")
                }
                "a" -> {
                    val annotation = node.attribute("href")?.value ?: return
                    pushStringAnnotation(tag = "URL", annotation = annotation)
                    withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                        append(node.text())
                    }
                    pop()
                }
                else -> {
                    Napier.e { "Unknown tag: ${node.tagName()}" }
                }
            }
        }
    }
}

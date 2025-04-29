package me.andannn.aozora.core.parser

import me.andannn.aozora.core.parser.html.HtmlLineParser
import me.andannn.aozora.core.parser.plaintext.PlainTextLineParser

/**
 * Create block parser.
 */
fun createBlockParser(isHtml: Boolean): AozoraBlockParser =
    DefaultAozoraBlockParser(
        parser = if (isHtml) HtmlLineParser else PlainTextLineParser
    )
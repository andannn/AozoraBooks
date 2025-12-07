/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.Page
import kotlin.test.Test
import kotlin.test.assertEquals

class LayoutPageBuilderTest {
    @Test
    fun testPageLayoutBuilder() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(builder, textBlockOf("123456"), FillResult.FillContinue)
        builder.build().verify(
            arrayOf(
                arrayOf("12345"),
                arrayOf("6"),
            ),
        )
    }

    @Test
    fun testPageLayoutBuilder_Filled() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(
            builder,
            textBlockOf("12345678901"),
            FillResult.Filled(remainBlock = textBlockOf("1")),
        )
        builder.build().verify(
            arrayOf(
                arrayOf("12345"),
                arrayOf("67890"),
            ),
        )
    }

    @Test
    fun testPageLayoutBuilder_With_Indent() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(
            builder,
            textBlockOf("1234567", indent = 2),
            FillResult.Filled(remainBlock = textBlockOf("7", indent = 2)),
        )
        builder.build().verify(
            arrayOf(
                arrayOf("indent_2", "123"),
                arrayOf("indent_2", "456"),
            ),
        )
    }

    @Test
    fun testPageLayoutBuilder_With_MaxCharacterPerLine() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(
            builder,
            textBlockOf("1234567", maxCharacterPerLine = 3),
            FillResult.Filled(remainBlock = textBlockOf("7", maxCharacterPerLine = 3)),
        )
        builder.build().verify(
            arrayOf(
                arrayOf("123"),
                arrayOf("456"),
            ),
        )
    }

    @Test
    fun testPageLayoutBuilder_With_MaxCharacterPerLine_And_Indent() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(
            builder,
            textBlockOf("1234567", maxCharacterPerLine = 3, indent = 1),
            FillResult.Filled(remainBlock = textBlockOf("7", maxCharacterPerLine = 3, indent = 1)),
        )
        builder.build().verify(
            arrayOf(
                arrayOf("indent_1", "123"),
                arrayOf("indent_1", "456"),
            ),
        )
    }

    @Test
    fun testPageLayoutBuilder_With_Ruby() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(builder, textBlockOf("1234"), FillResult.FillContinue)
        addBlockAndVerify(builder, blockOf(ruby("123")), FillResult.FillContinue)
        builder.build().verify(
            arrayOf(
                arrayOf("1234"),
                arrayOf("123"),
            ),
        )
    }

    @Test
    fun testPageLayoutBuilder_With_LineBreak() {
        val builder = createBuilder(20.dp, 50.dp, 10.dp)
        addBlockAndVerify(builder, blockOf(text("123"), AozoraElement.LineBreak), FillResult.FillContinue)
        addBlockAndVerify(builder, textBlockOf("1234"), FillResult.FillContinue)
        builder.build().verify(
            arrayOf(
                arrayOf("123"),
                arrayOf("1234"),
            ),
        )
    }
}

private fun Page.LayoutPage.verify(expect: Array<Array<String>>) {
    lines.forEachIndexed { index, line ->
        line.line.verify(*expect[index])
    }
}

private fun addBlockAndVerify(
    builder: LayoutPageBuilder,
    element: AozoraBlock,
    expect: FillResult,
) {
    with(builder) {
        assertEquals(expect, tryAddBlock(element))
    }
}

private fun createBuilder(
    width: Dp,
    height: Dp,
    textSize: Dp,
) = LayoutPageBuilder(
    fullWidth = width,
    fullHeight = height,
    scopeBuilder = {
        dummyElementMeasureScope(textSize)
    },
)

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import me.andannn.aozora.core.domain.model.AozoraElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class LineBuilderTest {
    @Test
    fun testLineBuilder() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp)
            addElementsAndVerify(builder, text("123"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("456789"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("0"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("1"), FillResult.Filled(text("1")))
            builder.verify("123", "456789", "0")
        }

    @Test
    fun testLineBuilderWhenEndDivided() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp)
            addElementsAndVerify(builder, text("1234567"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("1234"), FillResult.Filled(text("4")))
            builder.verify("1234567", "123")
        }

    @Test
    fun testBreakLine() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp)
            addElementsAndVerify(builder, text("1234"), FillResult.FillContinue)
            addElementsAndVerify(builder, AozoraElement.LineBreak, FillResult.Filled())
            addElementsAndVerify(builder, text("1234"), FillResult.Filled(text("1234")))
            builder.verify("1234")
        }

    @Test
    fun maxTextLimitTest() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp, maxCharacterPerLine = 5)
            addElementsAndVerify(builder, text("1234"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("1234"), FillResult.Filled(text("234")))
            builder.verify("1234", "1")
        }

    @Test
    fun maxTextLimitTest2() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp, maxCharacterPerLine = 12)
            addElementsAndVerify(builder, text("123456789"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("12"), FillResult.Filled(text("2")))
            builder.verify("123456789", "1")
        }

    @Test
    fun indentMustAddFirst(): Unit =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp)
            addElementsAndVerify(builder, text("1234"), FillResult.FillContinue)
            assertFails(
                "indent, and image can only be add to new line",
            ) {
                addElementsAndVerify(builder, AozoraElement.Indent(1), FillResult.FillContinue)
            }
        }

    @Test
    fun indentTest() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp)
            addElementsAndVerify(builder, AozoraElement.Indent(3), FillResult.FillContinue)
            addElementsAndVerify(builder, text("4567"), FillResult.FillContinue)
            addElementsAndVerify(builder, text("1234"), FillResult.Filled(text("4")))
            builder.verify("indent_3", "4567", "123")
        }

    @Test
    fun rubyCanNotBeDivided() =
        with(dummyElementMeasureScope(10.dp)) {
            val builder = LineBuilder(100.dp)
            addElementsAndVerify(builder, text("1234567"), FillResult.FillContinue)
            addElementsAndVerify(builder, ruby("1234"), FillResult.Filled(ruby("1234")))
            builder.verify("1234567")
        }
}

private fun text(text: String) = AozoraElement.Text(text)

private fun ruby(text: String) = AozoraElement.Ruby(text, "")

private fun ElementMeasureScope.addElementsAndVerify(
    builder: LineBuilder,
    element: AozoraElement,
    expect: FillResult,
) {
    with(builder) {
        assertEquals(expect, tryAdd(element))
    }
}

private fun LineBuilder.verify(vararg expects: String) {
    build().elements.forEachIndexed { index, element ->
        when (element) {
            is AozoraElement.Text -> {
                assertEquals(expects[index], element.text)
            }

            is AozoraElement.Indent -> {
                assertEquals(expects[index], "indent_${element.count}")
            }

            is AozoraElement.Ruby -> {
            }

            else -> {}
        }
    }
}

private fun dummyElementMeasureScope(textSize: Dp) =
    object : ElementMeasureScope {
        override fun measure(element: AozoraElement): ElementMeasureResult {
            val height =
                when (element) {
                    is AozoraElement.BaseText -> {
                        element.length.times(textSize)
                    }

                    is AozoraElement.Indent -> {
                        textSize.times(element.count)
                    }

                    else -> {
                        0.dp
                    }
                }
            return ElementMeasureResult(
                widthDp = textSize,
                heightDp = height,
                fontStyle = null,
            )
        }
    }

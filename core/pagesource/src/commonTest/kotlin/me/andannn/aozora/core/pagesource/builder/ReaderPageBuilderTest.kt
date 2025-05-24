/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.builder

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.PageMetaData
import me.andannn.aozora.core.domain.model.TopMargin
import me.andannn.aozora.core.pagesource.measure.ElementMeasureResult
import me.andannn.aozora.core.pagesource.page.AozoraBlock
import me.andannn.aozora.core.pagesource.page.FillResult
import me.andannn.aozora.core.pagesource.page.LayoutPageBuilder
import me.andannn.aozora.core.pagesource.page.LineBuilder
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ReaderPageBuilderTest {
    private lateinit var lineBuilder: LineBuilder
    private lateinit var pageBuilder: LayoutPageBuilder
    private val fontStyle = FontStyle(FontType.Companion.DEFAULT, 16.dp, 16.dp, 1.5f)

    private val dummySizeOf: (AozoraElement) -> ElementMeasureResult = {
        val size =
            when (it) {
                is AozoraElement.Indent -> 50.dp to (it.count * 10f).dp
                else -> 50.dp to (it.length * 10f).dp
            }
        ElementMeasureResult(
            widthDp = size.first,
            heightDp = size.second,
            fontStyle,
        )
    }

    private val dummyPaperLayout =
        object : PageMetaData {
            override val originalHeight: Dp
                get() = 100.dp
            override val originalWidth: Dp
                get() = 100.dp
            override val renderHeight: Dp
                get() = 100.dp
            override val renderWidth: Dp
                get() = 100.dp
            override val offset: Pair<Dp, Dp>
                get() = 0.dp to 0.dp
            override val fontSizeLevel: FontSizeLevel
                get() = FontSizeLevel.LEVEL_4
            override val lineSpacing: LineSpacing
                get() = LineSpacing.MEDIUM
            override val fontType: FontType
                get() = FontType.Companion.DEFAULT
            override val additionalTopMargin: TopMargin
                get() = TopMargin.MEDIUM
        }

    @BeforeTest
    fun setUp() {
        lineBuilder =
            LineBuilder(
                maxDp = 100.dp,
                measure = {
                    ElementMeasureResult(
                        widthDp = 30.dp,
                        heightDp = 30.dp,
                        fontStyle,
                    )
                },
            )
        pageBuilder =
            LayoutPageBuilder(
                meta = dummyPaperLayout,
                measurer = { element, style ->
                    ElementMeasureResult(
                        50.dp,
                        (10f * element.length).dp,
                        fontStyle,
                    )
                },
            )
    }

    @Test
    fun testReaderLineBuilder() {
        val builder = lineBuilder
        repeat(3) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text("tes")))
        }
        assertEquals(
            FillResult.Filled(AozoraElement.Text("es")),
            builder.tryAdd(AozoraElement.Text("tes")),
        )
        assertEquals("testestest", builder.build().fullText)
    }

    @Test
    fun testLinkBreak() {
        val builder = lineBuilder
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text("tes")))
        assertEquals(FillResult.Filled(), builder.tryAdd(AozoraElement.LineBreak))
    }

    @Test
    fun testRubyCanNotBeDivide() {
        val builder = lineBuilder
        repeat(3) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text("tes")))
        }
        assertEquals(
            FillResult.Filled(AozoraElement.Ruby("tes", "ruby")),
            builder.tryAdd(AozoraElement.Ruby("tes", "ruby")),
        )
    }

    @Test
    fun testBuildPage() {
        val builder = pageBuilder
        repeat(20) {
            assertEquals(
                FillResult.FillContinue,
                builder.tryAddElement(AozoraElement.Text("1"), 0, dummySizeOf),
            )
        }
        assertEquals(
            FillResult.Filled(AozoraElement.Text("A")),
            builder.tryAddElement(AozoraElement.Text("A"), 0, dummySizeOf),
        )
        val page = builder.build()
        assertEquals(2, page.lines.size)
    }

    @Test
    fun testPageLineBreak() {
        val builder = pageBuilder
        repeat(5) {
            assertEquals(
                FillResult.FillContinue,
                builder.tryAddElement(AozoraElement.Text("1"), 0, dummySizeOf),
            )
        }
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddElement(AozoraElement.LineBreak, 0, dummySizeOf),
        )
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddElement(AozoraElement.LineBreak, 0, dummySizeOf),
        )
        assertEquals(
            FillResult.Filled(AozoraElement.LineBreak),
            builder.tryAddElement(AozoraElement.LineBreak, 0, dummySizeOf),
        )
    }

    @Test
    fun testPageBreak() {
        val builder = pageBuilder
        repeat(5) {
            assertEquals(
                FillResult.FillContinue,
                builder.tryAddElement(AozoraElement.Text(it.toString()), 0, dummySizeOf),
            )
        }
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddElement(AozoraElement.PageBreak, 0, dummySizeOf),
        )
        assertEquals(
            FillResult.Filled(AozoraElement.Text("a")),
            builder.tryAddElement(AozoraElement.Text("a"), 0, dummySizeOf),
        )
    }

    @Test
    fun testRubyShowNoBeDivided() {
        val builder = pageBuilder
        repeat(9) {
            assertEquals(
                FillResult.FillContinue,
                builder.tryAddElement(AozoraElement.Text(it.toString()), 0, dummySizeOf),
            )
        }
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddElement(AozoraElement.Ruby("tes", "ruby"), 0, dummySizeOf),
        )
        val page = builder.build()
        assertEquals(2, page.lines.size)
        assertEquals(AozoraElement.Ruby("tes", "ruby"), page.lines[1].elements[0])
    }

    @Test
    fun testIndent() {
        val builder = pageBuilder
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddElement(AozoraElement.Indent(5), 0, dummySizeOf),
        )
        repeat(5) {
            assertEquals(
                FillResult.FillContinue,
                builder.tryAddElement(AozoraElement.Text(it.toString()), 0, dummySizeOf),
            )
        }
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddElement(AozoraElement.Text("1"), 0, dummySizeOf),
        )
        val page = builder.build()
        assertEquals(2, page.lines.size)
        assertEquals(5, (page.lines[0].elements[0] as AozoraElement.Indent).count)
    }

    @Test
    fun testAddBlock() {
        val builder = pageBuilder

        val block =
            AozoraBlock.TextBlock(
                blockIndex = 0,
                textStyle = AozoraTextStyle.HEADING_LARGE,
                elements = emptyList(),
            )
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddBlock(
                block.copy(
                    elements =
                        buildList {
                            repeat(20) {
                                add(AozoraElement.Text("1"))
                            }
                        },
                ),
            ),
        )
        assertEquals(
            AozoraElement.Text("a"),
            (builder.tryAddBlock(block.copy(elements = listOf(AozoraElement.Text("a")))) as FillResult.Filled).remainBlock?.elements[0],
        )
        val page = builder.build()
        assertEquals(2, page.lines.size)
    }

    @Test
    fun testAddBlockWithIndent() {
        val builder = pageBuilder

        val block =
            AozoraBlock.TextBlock(
                indent = 5,
                elements = emptyList(),
                textStyle = AozoraTextStyle.HEADING_LARGE,
                blockIndex = 0,
            )
        assertEquals(
            FillResult.FillContinue,
            builder.tryAddBlock(
                block.copy(
                    elements =
                        buildList {
                            repeat(10) {
                                add(AozoraElement.Text("1"))
                            }
                        },
                ),
            ),
        )
        assertEquals(
            AozoraElement.Text("a"),
            (builder.tryAddBlock(block.copy(elements = listOf(AozoraElement.Text("a")))) as FillResult.Filled).remainBlock?.elements[0],
        )
        val page = builder.build()
        assertEquals(2, page.lines.size)
    }
}

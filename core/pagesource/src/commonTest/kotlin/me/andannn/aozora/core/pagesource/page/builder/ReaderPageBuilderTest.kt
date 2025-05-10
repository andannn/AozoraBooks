/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page.builder

import androidx.compose.ui.geometry.Size
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.AozoraTextStyle
import me.andannn.aozora.core.data.common.Block
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.PageMetaData
import me.andannn.aozora.core.data.common.TopMargin
import me.andannn.aozora.core.pagesource.measure.ElementMeasureResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ReaderPageBuilderTest {
    private lateinit var lineBuilder: LineBuilder
    private lateinit var pageBuilder: LayoutPageBuilder
    private val fontStyle = FontStyle(FontType.Companion.DEFAULT, 16f, 16f, 1.5f)

    private val dummySizeOf: (AozoraElement) -> ElementMeasureResult = {
        val size =
            when (it) {
                is AozoraElement.Indent -> Size(50f, it.count * 10f)
                else -> Size(50f, it.length * 10f)
            }
        ElementMeasureResult(
            size,
            fontStyle,
        )
    }

    private val dummyPaperLayout =
        object : PageMetaData {
            override val originalHeight: Float
                get() = 100f
            override val originalWidth: Float
                get() = 100f
            override val renderHeight: Float
                get() = 100f
            override val renderWidth: Float
                get() = 100f
            override val offset: Pair<Float, Float>
                get() = 0f to 0f
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
                maxPx = 100f,
                measure = {
                    ElementMeasureResult(
                        Size(30f, 30f),
                        fontStyle,
                    )
                },
            )
        pageBuilder =
            LayoutPageBuilder(
                meta = dummyPaperLayout,
                measurer = { element, style ->
                    ElementMeasureResult(
                        Size(50f, 10f * element.length),
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
            Block.Paragraph(
                blockIndex = 0,
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
            Block.Heading(
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

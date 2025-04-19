package me.andannn.aosora.common.pager

import androidx.compose.ui.geometry.Size
import me.andannn.aosora.common.parser.AozoraElement
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ReaderPageBuilderTest {
    private lateinit var lineBuilder: ReaderLineBuilder
    private lateinit var pageBuilder: ReaderPageBuilder

    @BeforeTest
    fun setUp() {
        lineBuilder = ReaderLineBuilder(
            maxPx = 100,
            measure = {
                Size(30f, 30f)
            },
            spacing = { Spacing(0, 0) }
        )
        pageBuilder = ReaderPageBuilder(
            meta = ReaderPageMeta(100, 100),
            measure = {
                when (it) {
                    is AozoraElement.Indent -> Size(50f, it.count * 10f)
                    else -> Size(50f, it.length * 10f)
                }
            },
            spacing = {
                Spacing(0, 0)
            }
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
            builder.tryAdd(AozoraElement.Text("tes"))
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
            builder.tryAdd(AozoraElement.Ruby("tes", "ruby"))
        )
    }

    @Test
    fun testBuildPage() {
        val builder = pageBuilder
        repeat(20) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text("1")))
        }
        assertEquals(
            FillResult.Filled(AozoraElement.Text("A")),
            builder.tryAdd(AozoraElement.Text("A"))
        )
        val page = builder.build()
        assertEquals(2, (page as ReaderPage.MainContentPage).lines.size)
    }

    @Test
    fun testPageLineBreak() {
        val builder = pageBuilder
        repeat(5) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text("1")))
        }
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.LineBreak))
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.LineBreak))
        assertEquals(
            FillResult.Filled(AozoraElement.LineBreak),
            builder.tryAdd(AozoraElement.LineBreak)
        )
    }

    @Test
    fun testPageBreak() {
        val builder = pageBuilder
        repeat(5) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text(it.toString())))
        }
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.PageBreak))
        assertEquals(
            FillResult.Filled(AozoraElement.Text("a")),
            builder.tryAdd(AozoraElement.Text("a"))
        )
    }

    @Test
    fun testRubyShowNoBeDivided() {
        val builder = pageBuilder
        repeat(9) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text(it.toString())))
        }
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Ruby("tes", "ruby")))
        val page = builder.build()
        assertEquals(2, (page as ReaderPage.MainContentPage).lines.size)
        assertEquals(AozoraElement.Ruby("tes", "ruby"), page.lines[1].elements[0])
    }

    @Test
    fun testIndent() {
        val builder = pageBuilder
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Indent(5)))
        repeat(5) {
            assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text(it.toString())))
        }
        assertEquals(FillResult.FillContinue, builder.tryAdd(AozoraElement.Text("1")))
        val page = builder.build()
        assertEquals(2, (page as ReaderPage.MainContentPage).lines.size)
        assertEquals(5, (page.lines[0].elements[0] as AozoraElement.Indent).count)
    }
}
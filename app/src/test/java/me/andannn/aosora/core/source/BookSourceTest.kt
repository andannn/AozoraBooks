package me.andannn.aosora.core.source

import me.andannn.aosora.core.common.PageMetaData
import me.andannn.aosora.core.pager.fullText
import java.nio.file.Paths
import kotlin.test.BeforeTest
import kotlin.test.Test

class BookSourceTest {
    private lateinit var bookSource: BookSource

    @BeforeTest
    fun setUp() {
        bookSource = BookSource(Paths.get("src/test/resources/49947_ruby_39182"))
    }

    @Test
    fun testBookSource() {
//        bookSource.pageSource(PageMetaData(1000f, 1000f)).forEach {
//            println(it.fullText)
//            println("------------------------------------------------------------------------")
//        }
    }
}
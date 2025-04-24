package me.andannn.aosora.core.source

import java.nio.file.Paths
import kotlin.test.BeforeTest
import kotlin.test.Test

class BookSourceTest {
    private lateinit var bookPageSource: BookPageSource

    @BeforeTest
    fun setUp() {
        bookPageSource = BookPageSource(Paths.get("src/test/resources/49947_ruby_39182"))
    }

    @Test
    fun testBookSource() {
//        bookSource.pageSource(PageMetaData(1000f, 1000f)).forEach {
//            println(it.fullText)
//            println("------------------------------------------------------------------------")
//        }
    }
}
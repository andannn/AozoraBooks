package com.andannn.aozora.core.service.impl

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import me.andannn.aozora.core.service.clientBuilder
import me.andannn.aozora.core.service.impl.AozoraServiceImpl
import me.andannn.aozora.core.service.impl.parseBookListFromOpeningBooks
import me.andannn.core.util.readString
import kotlin.test.Test

class AozoraServiceImplTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val service =
        AozoraServiceImpl(
            httpClient = clientBuilder(),
        )

    @Test
    fun getBookListPageTest() {
        testScope.runTest {
            val result = service.getBookListOfKanaByPage("a", 1)
            println(result)
        }
    }

    @Test
    fun parseBookColumnTest() {
        val result =
            parseBookListFromOpeningBooks(
                Path("src/commonTest/resources/test.html").readString("Utf-8"),
            )
        println(result)
    }
}

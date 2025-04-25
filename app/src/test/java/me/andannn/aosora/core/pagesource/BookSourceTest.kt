package me.andannn.aosora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import kotlin.test.BeforeTest
import kotlin.test.Test

class BookSourceTest {
    private lateinit var bookPageSource: LazyBookPageSource<String>
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private lateinit var settledFlow: Flow<String>

    @BeforeTest
    fun setUp() {
        settledFlow = flow {
            delay(500)
            emit("after 0")
            delay(500)
            emit("after 1")
            delay(500)
            emit("after 2")
            delay(500)
            emit("after 3")
        }

        bookPageSource = object : LazyBookPageSource<String>(testScope, settledFlow) {
            override fun generatePageFlowBefore() = flow {
                repeat(10) {
                    delay(100)
                    emit("before $it")
                }
            }

            override fun generatePageFlowAfter() = flow {
                repeat(10) {
                    delay(100)
                    emit("after $it")
                }
            }
        }
    }

    @Test
    fun testBookSource() = testScope.runTest {
        bookPageSource.pagerSnapShotFlow.collect {
            println(it)
        }
    }
}
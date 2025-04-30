package me.andannn.aozora.core.pagesource.raw

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import me.andannn.aozora.core.data.common.AozoraBookCard
import org.koin.core.context.startKoin
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class RemoteOrCacheBookRawSourceTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val randomIndex: Int = Random.Default.nextInt()

    private lateinit var testPath: Path

    @BeforeTest
    fun setUp() {
        testPath = Path("$SystemTemporaryDirectory/test_download_$randomIndex")
        SystemFileSystem.createDirectories(path = testPath)
        println(testPath)
    }

    @AfterTest
    fun tearDown() {

    }

    @Test
    fun testParseHtmlMeta() {
        processParseHtml(Path("src/androidUnitTest/resources/test.html"), Path("$testPath"))
    }
}
package me.andannn.aosora.core.pagesource.raw

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlinx.serialization.json.Json
import me.andannn.aosora.core.common.model.AozoraBookCard
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
    fun testFetchBook() = testScope.runTest {
        val remoteOrCacheBookRawSource = RemoteOrCacheBookRawSource(
            card = AozoraBookCard(
                "id",
                zipUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_ruby_78315.zip",
                htmlUrl = "https://www.aozora.gr.jp/cards/002238/files/61411_78314.html",
            ),
            scope = testScope,
            dispatcher = dispatcher,
            cacheDictionary = testPath
        )

        remoteOrCacheBookRawSource.getRawSource()
    }

    @Test
    fun testParseHtmlMeta() {
        processParseHtml(Path("src/test/resources/test.html"), Path("$testPath"))
    }
}
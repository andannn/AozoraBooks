package me.andannn.aosora.core.pagesource

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.readLine
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pagesource.page.generatePageFlow
import me.andannn.aosora.core.parser.AozoraBlockParser

abstract class BufferedLazyPageSource(
    private val meta: PageMetaData,
    scope: CoroutineScope,
    private val progress: Long,
    settledPageFlow: Flow<AozoraPage?>,
) : LazyBookPageSource<AozoraPage>(
    scope = scope,
    settledPageFlow = settledPageFlow
) {
    abstract val bookSource: Source
    abstract val parser: AozoraBlockParser

    override fun generatePageFlowBefore(): Flow<AozoraPage> {
        val buffer = Buffer()
        bookSource.peek().readAtMostTo(buffer, progress)
        return generatePageFlow(
            meta = meta,
            aozoraBlockParser = parser,
            lineSequence = buffer.parseAsReversedLineSequence()
        )
    }

    override fun generatePageFlowAfter(): Flow<AozoraPage> {
        val source = bookSource.peek().buffered()
        source.skip(progress)
        return generatePageFlow(
            meta = meta,
            aozoraBlockParser = parser,
            lineSequence = source.parseAsLineSequence()
        )
    }
}

private fun Source.parseAsLineSequence() = sequence<String> {
    while (!exhausted()) {
        yield(readLine() ?: continue)
    }
}

private fun Source.parseAsReversedLineSequence() = sequence<String> {
    while (!exhausted()) {
        yield(readLine() ?: continue)
    }
}.toList().reversed().asSequence()

package me.andannn.aozora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.buffered
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.data.common.PageContext
import me.andannn.aozora.core.pagesource.page.builder.createPageBuilder
import me.andannn.aozora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aozora.core.parser.AozoraBlockParser
import me.andannn.aozora.core.parser.lineSequence

abstract class BufferedLazyPageSource(
    private val meta: PageContext,
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
        return createPageFlowFromSequence(
            builderFactory = {
                createPageBuilder(meta)
            },
            blockSequenceFlow = buffer.parseAsReversedLineSequence()
                .map { parser.parseLineAsBlock(it) }.asFlow()
        )
    }

    override fun generatePageFlowAfter(): Flow<AozoraPage> {
        val source = bookSource.peek().buffered()
        source.skip(progress)
        return createPageFlowFromSequence(
            builderFactory = {
                createPageBuilder(meta)
            },
            blockSequenceFlow = source.parseAsLineSequence().map { parser.parseLineAsBlock(it) }
                .asFlow()
        )
    }
}

private fun Source.parseAsLineSequence() = lineSequence()

private fun Source.parseAsReversedLineSequence() =
    lineSequence().toList().reversed().asSequence()

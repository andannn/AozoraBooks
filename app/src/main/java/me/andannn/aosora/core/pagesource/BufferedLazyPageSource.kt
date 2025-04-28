package me.andannn.aosora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.buffered
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.common.model.AozoraPage.AozoraRoughPage
import me.andannn.aosora.core.common.model.PageContext
import me.andannn.aosora.core.common.util.lineSequence
import me.andannn.aosora.core.pagesource.page.builder.createPageBuilder
import me.andannn.aosora.core.pagesource.page.createPageFlowFromSequence
import me.andannn.aosora.core.parser.AozoraBlockParser

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
                createPageBuilder<AozoraRoughPage>(meta)
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
                createPageBuilder<AozoraRoughPage>(meta)
            },
            blockSequenceFlow = source.parseAsLineSequence().map { parser.parseLineAsBlock(it) }
                .asFlow()
        )
    }
}

private fun Source.parseAsLineSequence() = lineSequence()

private fun Source.parseAsReversedLineSequence() =
    lineSequence().toList().reversed().asSequence()

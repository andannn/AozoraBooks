package me.andannn.aosora.core.pager

import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.parser.AozoraBlock
import me.andannn.aosora.core.parser.AozoraBlockParser

suspend fun SequenceScope<AozoraPage>.generatePageSequence(
    aozoraBlockParser: AozoraBlockParser,
    lineSequence: Sequence<String>,
    meta: PageMetaData
) {
    var pageBuilder: ReaderPageBuilder? = null

    suspend fun SequenceScope<AozoraPage>.tryAdd(block: AozoraBlock) {
        val builder = pageBuilder ?: createDefaultReaderPageBuilder(meta)
            .also { pageBuilder = it }

        when (val result = builder.tryAddBlock(block)) {
            FillResult.FillContinue -> return
            is FillResult.Filled -> {
                yield(builder.build())

                pageBuilder = null

                val remainBlock = result.remainBlock
                if (remainBlock != null) {
                    tryAdd(remainBlock)
                }
            }
        }
    }

    for (line in lineSequence) {
        val block = aozoraBlockParser.parseLineAsBlock(line)
        tryAdd(block)
    }

    if (pageBuilder != null) {
        yield(pageBuilder!!.build())
    }
}

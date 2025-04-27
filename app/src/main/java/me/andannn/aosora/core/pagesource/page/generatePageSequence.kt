package me.andannn.aosora.core.pagesource.page

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.pagesource.page.builder.FillResult
import me.andannn.aosora.core.pagesource.page.builder.PageBuilder

/**
 * create page flow from [blockSequenceFlow].
 */
fun <T: AozoraPage> createPageFlowFromSequence(
    blockSequenceFlow: Flow<AozoraBlock>,
    builder: () -> PageBuilder<T>,
) = flow<AozoraPage> {
    var pageBuilder: PageBuilder<T>? = null

    suspend fun tryAdd(block: AozoraBlock) {
        val builder = pageBuilder ?: builder()
            .also { pageBuilder = it }

        when (val result = builder.tryAddBlock(block)) {
            FillResult.FillContinue -> return
            is FillResult.Filled -> {
                emit(builder.build())

                pageBuilder = null

                val remainBlock = result.remainBlock
                if (remainBlock != null) {
                    tryAdd(remainBlock)
                }
            }
        }
    }

    blockSequenceFlow.collect { block ->
        tryAdd(block)
    }

    if (pageBuilder != null) {
        emit(pageBuilder!!.build())
    }
}

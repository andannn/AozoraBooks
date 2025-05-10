/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.page

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.andannn.aozora.core.data.common.AozoraPage

/**
 * create page flow from [blockSequenceFlow].
 */
internal fun <T : AozoraPage> createPageFlowFromSequence(
    blockSequenceFlow: Flow<AozoraBlock>,
    builderFactory: () -> PageBuilder<T>,
) = flow<T> {
    var pageBuilder: PageBuilder<T>? = null

    suspend fun tryAdd(block: AozoraBlock) {
        val builder =
            pageBuilder ?: builderFactory()
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
        if (block.elements.isNotEmpty()) {
            tryAdd(block)
        }
    }

    if (pageBuilder != null) {
        emit(pageBuilder!!.build())
    }
}

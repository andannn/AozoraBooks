package me.andannn.aozora.core.data.internal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.data.AozoraContentsRepository
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.BookColumnItem
import me.andannn.aozora.core.service.AozoraService

class AozoraContentsRepositoryImpl(
    private val aozoraService: AozoraService,
) : AozoraContentsRepository {
    override fun getBookListPagingFlow(kana: String): Flow<PagingData<BookColumnItem>> =
        Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { BookListPagingSource(kana, aozoraService) },
        ).flow

    override suspend fun getBookCard(
        cardId: String,
        groupId: String,
    ): AozoraBookCard = aozoraService.getBookCard(groupId = groupId, cardId = cardId)
}

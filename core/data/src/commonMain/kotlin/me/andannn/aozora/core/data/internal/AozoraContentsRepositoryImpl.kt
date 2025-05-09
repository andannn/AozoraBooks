package me.andannn.aozora.core.data.internal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.data.AozoraContentsRepository
import me.andannn.aozora.core.data.common.BookColumnItem
import org.koin.mp.KoinPlatform.getKoin

class AozoraContentsRepositoryImpl : AozoraContentsRepository {
    override fun getBookListPaging(kana: String): Flow<PagingData<BookColumnItem>> =
        Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { BookListPagingSource(kana, getKoin().get()) },
        ).flow
}

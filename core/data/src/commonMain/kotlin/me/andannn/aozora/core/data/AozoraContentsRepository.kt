package me.andannn.aozora.core.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.andannn.aozora.core.data.common.BookColumnItem

interface AozoraContentsRepository {
    fun getBookListPaging(kana: String): Flow<PagingData<BookColumnItem>>
}

package me.andannn.aozora.core.pagesource.raw

import kotlinx.coroutines.flow.Flow
import kotlinx.io.files.Path
import me.andannn.aozora.core.data.common.Block
import me.andannn.aozora.core.data.common.BookMeta

private const val TAG = "BookRawSource"

/**
 * Book raw source.
 */
interface BookRawSource {
    /**
     * get raw source of book.
     */
    suspend fun getRawSource(): Flow<Block>

    /**
     * get image uri by path.
     */
    suspend fun getImageUriByPath(path: String): Path?

    /**
     * get book meta.
     */
    suspend fun getBookMeta(): BookMeta

    fun dispose()
}

package me.andannn.aosora.core.pagesource.raw

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import me.andannn.aosora.core.common.model.AozoraBlock
import me.andannn.aosora.core.common.model.BookMeta

private const val TAG = "BookRawSource"

/**
 * Book raw source.
 */
interface BookRawSource {
    /**
     * get raw source of book.
     */
    suspend fun getRawSource(): Flow<AozoraBlock>

    /**
     * get image uri by path.
     */
    suspend fun getImageUriByPath(path: String): Uri?

    /**
     * get book meta.
     */
    suspend fun getBookMeta(): BookMeta
}

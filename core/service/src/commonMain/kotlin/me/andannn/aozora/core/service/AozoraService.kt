package me.andannn.aozora.core.service

import me.andannn.aozora.core.data.common.BookColumnItem

interface AozoraService {
    suspend fun getPageCountOfKana(kana: String): Int

    suspend fun getBookListOfKanaByPage(
        kana: String,
        page: Int,
    ): List<BookColumnItem>
}

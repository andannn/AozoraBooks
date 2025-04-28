package me.andannn.aosora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.andannn.aosora.core.common.model.AozoraBookCard
import me.andannn.aosora.core.common.model.AozoraPage
import me.andannn.aosora.core.pagesource.raw.RemoteOrCacheBookRawSource

/**
 * Book page source.
 *
 * @property useHtmlFirst If true, use html first. Otherwise, use plain text.
 */
private class AozoraBookPageSource<out T : AozoraPage>(
    card: AozoraBookCard,
    scope: CoroutineScope,
    private val useHtmlFirst: Boolean = true,
) : CachedLinerPageSource<T>(
    rawSource = RemoteOrCacheBookRawSource(card, scope, useHtmlFirst = useHtmlFirst, dispatcher = Dispatchers.IO)
)
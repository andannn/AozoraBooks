package me.andannn.aozora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.andannn.aozora.core.data.common.AozoraBookCard
import me.andannn.aozora.core.data.common.AozoraPage
import me.andannn.aozora.core.pagesource.raw.RemoteOrCacheBookRawSource

/**
 * Book page source.
 *
 * @property useHtmlFirst If true, use html first. Otherwise, use plain text.
 */
abstract class AozoraBookPageSource<out T : AozoraPage>(
    card: AozoraBookCard,
    scope: CoroutineScope,
    private val useHtmlFirst: Boolean = true,
    useRoughPageBuilder: Boolean
) : CachedLinerPageSource<T>(
    rawSource = RemoteOrCacheBookRawSource(
        card,
        scope,
        useHtmlFirst = useHtmlFirst,
        dispatcher = Dispatchers.IO
    ),
    useRoughPageBuilder = useRoughPageBuilder,
)

class RoughPageSource(
    card: AozoraBookCard,
    scope: CoroutineScope,
    useHtmlFirst: Boolean = true,
) : AozoraBookPageSource<AozoraPage.AozoraRoughPage>(
    card = card,
    scope = scope,
    useHtmlFirst = useHtmlFirst,
    useRoughPageBuilder = true,
)

class LayoutPageSource(
    card: AozoraBookCard,
    scope: CoroutineScope,
    useHtmlFirst: Boolean = true,
) : AozoraBookPageSource<AozoraPage.AozoraLayoutPage>(
    card = card,
    scope = scope,
    useHtmlFirst = useHtmlFirst,
    useRoughPageBuilder = false,
)
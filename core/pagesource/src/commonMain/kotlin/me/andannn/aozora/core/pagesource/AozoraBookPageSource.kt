/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.andannn.aozora.core.data.common.BookPreviewInfo
import me.andannn.aozora.core.pagesource.raw.RemoteOrLocalCacheBookRawSource

/**
 * Book page source.
 */
class AozoraBookPageSource(
    card: BookPreviewInfo,
    scope: CoroutineScope,
) : CachedLinerPageSource(
        rawSource =
            RemoteOrLocalCacheBookRawSource(
                card,
                scope,
                dispatcher = Dispatchers.IO,
            ),
    )

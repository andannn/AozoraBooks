/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import me.andannn.platform.PlatformAnalytics

sealed class SyncEvent(
    val label: String,
) {
    data object SuccessBundle : SyncEvent("first_time_sync")

    data object SuccessServer : SyncEvent("sync_with_server_success")

    data object Fail : SyncEvent("sync_fail")

    data object Skip : SyncEvent("sync_skip")
}

fun PlatformAnalytics.logSyncEvent(result: SyncEvent) {
    logEvent("sync_event", params = mapOf("result" to result.label))
}

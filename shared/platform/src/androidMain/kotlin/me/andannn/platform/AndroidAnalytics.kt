/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.platform

import io.github.aakira.napier.Napier

private const val TAG = "AndroidAnalytics"

class AndroidAnalytics : PlatformAnalytics {
    override fun logEvent(
        event: String,
        params: Map<String, String>,
    ) {
        Napier.d(tag = TAG) { "logEvent: event :$event, params $params" }
//        Firebase.analytics.logEvent(
//            // name =
//            event,
//            // params =
//            Bundle().apply {
//                params.forEach { (key, value) ->
//                    this.putString(key, value)
//                }
//            },
//        )
    }

    override fun recordException(throwable: Throwable) {
//        Firebase.crashlytics.recordException(throwable)
    }
}

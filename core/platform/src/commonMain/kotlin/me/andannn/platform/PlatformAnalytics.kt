package me.andannn.platform

interface PlatformAnalytics {
    fun logEvent(
        event: String,
        params: Map<String, String> = emptyMap(),
    )
}

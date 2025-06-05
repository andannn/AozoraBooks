/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import org.koin.mp.KoinPlatform.getKoin

internal const val CSV_ZIP_URL = "https://www.aozora.gr.jp/index_pages/list_person_all_extended_utf8.zip"

suspend fun getCsvZipLastModifiedTime(): String {
    val client = getKoin().get<HttpClient>()
    val response =
        client.request(CSV_ZIP_URL) {
            method = HttpMethod.Head
        }
    return response.headers["last-modified"] ?: error("last-modified not found")
}

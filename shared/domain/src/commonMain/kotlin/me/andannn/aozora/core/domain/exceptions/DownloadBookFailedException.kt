/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.exceptions

class DownloadBookFailedException(bookTitle: String) : Exception("Download [$bookTitle] failed.")

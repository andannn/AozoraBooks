/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.exceptions

import me.andannn.aozora.core.domain.model.AozoraBookCard

class CopyRightRetainedException(
    val bookCard: AozoraBookCard,
) : Exception("${bookCard.id} have copyRight and no html.")

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.util

import kotlin.math.floor

fun Float.toPercentString() = floor(this.times(100)).toInt().toString() + "%"

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.MeasureHelper

class TextRenderAdapterV2(
    measureHelper: MeasureHelper,
) : BasicTextRenderAdapterV2<AozoraElement.Text>(measureHelper)

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.pagesource.measure

import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.core.domain.model.RenderSetting
import me.andannn.aozora.core.domain.model.resolveFontStyle

interface TextStyleCalculator {
    fun resolve(aozoraStyle: AozoraTextStyle): FontStyle
}

internal class TextStyleCalculatorImpl(
    private val renderSetting: RenderSetting,
) : TextStyleCalculator {
    private val fontStyleCache = mutableMapOf<AozoraTextStyle, FontStyle>()

    override fun resolve(aozoraStyle: AozoraTextStyle): FontStyle {
        return fontStyleCache[aozoraStyle]
            ?: aozoraStyle
                .resolveFontStyle(
                    fontSizeLevel = renderSetting.fontSizeLevel,
                    lineSpacing = renderSetting.lineSpacing,
                    fontType = renderSetting.fontType,
                ).also {
                    fontStyleCache[aozoraStyle] = it
                }
    }
}

/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin
import me.andannn.aozora.ui.common.theme.AozoraTheme

@Composable
@Preview
private fun ReaderSettingPreview() {
    AozoraTheme {
        ReaderSettingDialogContent(
            state =
                ReaderSettingState(
                    fontSizeLevel = FontSizeLevel.LEVEL_1,
                    fontType = FontType.NOTO_SANS,
                    topMargin = TopMargin.MEDIUM,
                    lineSpacing = LineSpacing.MEDIUM,
                    theme = ReaderTheme.GREEN_EYE_CARE,
                ),
            currentSelectIndex = 0,
        )
    }
}

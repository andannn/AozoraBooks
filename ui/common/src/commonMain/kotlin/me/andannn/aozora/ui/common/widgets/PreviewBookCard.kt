/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.andannn.aozora.ui.common.theme.NotoSerifJpFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewBookCard(
    modifier: Modifier = Modifier,
    title: String,
    author: String,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.aspectRatio(3 / 4f),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        onClick = onClick,
    ) {
        Box {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(18.dp),
                        ).padding(2.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(18.dp),
                        ).padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = NotoSerifJpFontFamily,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = author,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = NotoSerifJpFontFamily,
                )
                Spacer(modifier = Modifier.weight(2f))
            }

            Icon(
                modifier =
                    Modifier
                        .align(
                            Alignment.TopEnd,
                        ).graphicsLayer {
                            scaleY = 2f
                            translationX = -5.dp.toPx()
                            translationY = -2.dp.toPx()
                        },
                imageVector = Icons.Filled.Bookmark,
                contentDescription = null,
            )
        }
    }
}

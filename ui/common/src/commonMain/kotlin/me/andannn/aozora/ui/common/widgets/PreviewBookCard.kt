/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.ReadProgress

@Composable
fun PreviewBookCard(
    modifier: Modifier = Modifier,
    title: String,
    author: String,
    progress: ReadProgress,
    userMarkRead: Boolean,
    subTitle: String? = null,
    onClick: () -> Unit = {},
    onOptionClick: () -> Unit = {},
) {
    val progressFactor =
        remember(progress, userMarkRead) {
            return@remember when (progress) {
                ReadProgress.Done -> 1f
                ReadProgress.None -> 0f
                is ReadProgress.Reading ->
                    progress.progressFactor ?: 0f
            }
        }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.width(64.dp).background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                ),
            ) {
                Spacer(
                    modifier =
                        Modifier
                            .aspectRatio(3 / 4f)
                            .fillMaxSize()
                            .padding(8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp),
                            ).padding(2.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp),
                            ).padding(12.dp),
                )

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
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                subTitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = author,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .height(4.dp)
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                ) {
                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth(progressFactor)
                                .align(Alignment.BottomStart)
                                .fillMaxHeight()
                                .background(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.secondary,
                                ),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = onOptionClick,
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                )
            }
        }
    }
}

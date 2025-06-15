/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.common.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.andannn.aozora.core.domain.model.AozoraBookCard

@Composable
fun BookColumnItemView(
    item: AozoraBookCard,
    index: Int?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Surface(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            index?.let {
                Text("No.${index + 1}", style = MaterialTheme.typography.labelLarge)
            }
            Text(
                item.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            if (item.subTitle != null) {
                Text(item.subTitle!!, style = MaterialTheme.typography.bodyMedium)
            }
            if (item.haveCopyRight) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "＊著作権存続＊",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(2.dp))
            Text("著者：" + item.author, style = MaterialTheme.typography.bodySmall)
        }
    }
}

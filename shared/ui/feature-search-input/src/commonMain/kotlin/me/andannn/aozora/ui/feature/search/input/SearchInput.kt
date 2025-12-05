/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.search.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import me.andannn.aozora.ui.common.Presenter

@Composable
fun SearchInput(
    initialParam: String?,
    presenter: Presenter<SearchInputState> = retainSearchInputPresenter(initialParam),
    modifier: Modifier = Modifier,
) {
    SearchInput(
        state = presenter.present(),
        modifier = modifier,
    )
}

@Composable
private fun SearchInput(
    state: SearchInputState,
    modifier: Modifier = Modifier,
) {
    SearchInputContent(
        modifier = modifier,
        inputText = state.inputText,
        onEvent = state.evenSink,
    )
}

@Composable
private fun SearchInputContent(
    modifier: Modifier = Modifier,
    inputText: TextFieldValue,
    onEvent: (SearchInputUiEvent) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(modifier = Modifier.systemBarsPadding()) {
            Row(
                modifier = Modifier.height(72.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onEvent.invoke(SearchInputUiEvent.Back)
                    },
                ) {
                    Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = null)
                }

                TextField(
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    value = inputText,
                    onValueChange = {
                        onEvent.invoke(SearchInputUiEvent.OnValueChange(it))
                    },
                    placeholder = {
                        Text(
                            "作家・作品を検索",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search,
                        ),
                    keyboardActions =
                        KeyboardActions(
                            onSearch = {
                                onEvent.invoke(SearchInputUiEvent.OnConfirmSearch)
                            },
                        ),
                    colors =
                        TextFieldDefaults.colors().copy(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                        ),
                )
            }

            HorizontalDivider()
        }
    }
}

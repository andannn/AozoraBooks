/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.core.data.common.FontSizeLevel
import me.andannn.aozora.core.data.common.FontType
import me.andannn.aozora.core.data.common.LineSpacing
import me.andannn.aozora.core.data.common.ReaderTheme
import me.andannn.aozora.core.data.common.TopMargin
import me.andannn.aozora.core.data.common.next
import me.andannn.aozora.core.data.common.pre
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType
import org.koin.mp.KoinPlatform.getKoin

object ReaderSettingDialogId : DialogId {
    override val dialogType: DialogType = DialogType.ModalBottomSheet

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        ReaderSettingDialog()
    }
}

@Composable
fun rememberReaderSettingDialogPresenter(settingRepository: UserDataRepository = getKoin().get()) =
    remember(settingRepository) {
        ReaderSettingDialogPresenter(settingRepository)
    }

class ReaderSettingDialogPresenter(
    private val settingRepository: UserDataRepository,
) : Presenter<ReaderSettingState> {
    @Composable
    override fun present(): ReaderSettingState {
        val scope = rememberCoroutineScope()
        val fontSize =
            settingRepository.getFontSizeLevel().collectAsRetainedState(
                FontSizeLevel.DEFAULT,
            )
        val fontType =
            settingRepository.getFontFontType().collectAsRetainedState(
                FontType.DEFAULT,
            )
        val topMargin =
            settingRepository.getTopMargin().collectAsRetainedState(
                TopMargin.DEFAULT,
            )
        val lineSpacing =
            settingRepository.getLineSpacing().collectAsRetainedState(
                LineSpacing.DEFAULT,
            )
        val theme =
            settingRepository.getReaderTheme().collectAsRetainedState(
                ReaderTheme.DEFAULT,
            )

        return ReaderSettingState(
            fontSizeLevel = fontSize.value,
            fontType = fontType.value,
            topMargin = topMargin.value,
            lineSpacing = lineSpacing.value,
            theme = theme.value,
            eventSink = {
                when (it) {
                    is ReaderSettingUiEvent.OnChangeFontSize -> {
                        scope.launch {
                            settingRepository.setFontSizeLevel(it.fontSizeLevel)
                        }
                    }

                    is ReaderSettingUiEvent.OnChangeFontType -> {
                        scope.launch {
                            settingRepository.setFontType(it.fontType)
                        }
                    }

                    is ReaderSettingUiEvent.OnChangeLineSpacing ->
                        scope.launch {
                            settingRepository.setLineSpacing(it.lineSpacing)
                        }

                    is ReaderSettingUiEvent.OnChangeReaderTheme ->
                        scope.launch {
                            settingRepository.setReaderTheme(it.readerTheme)
                        }

                    is ReaderSettingUiEvent.OnChangeTopMargin ->
                        scope.launch {
                            settingRepository.setTopMargin(it.topMargin)
                        }
                }
            },
        )
    }
}

@Stable
data class ReaderSettingState(
    val fontSizeLevel: FontSizeLevel,
    val fontType: FontType,
    val topMargin: TopMargin,
    val lineSpacing: LineSpacing,
    val theme: ReaderTheme,
    val eventSink: (ReaderSettingUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface ReaderSettingUiEvent {
    data class OnChangeFontSize(
        val fontSizeLevel: FontSizeLevel,
    ) : ReaderSettingUiEvent

    data class OnChangeFontType(
        val fontType: FontType,
    ) : ReaderSettingUiEvent

    data class OnChangeTopMargin(
        val topMargin: TopMargin,
    ) : ReaderSettingUiEvent

    data class OnChangeLineSpacing(
        val lineSpacing: LineSpacing,
    ) : ReaderSettingUiEvent

    data class OnChangeReaderTheme(
        val readerTheme: ReaderTheme,
    ) : ReaderSettingUiEvent
}

@Composable
internal fun ReaderSettingDialog() {
    val state = rememberReaderSettingDialogPresenter().present()
    val currentSelectIndex =
        remember {
            mutableStateOf(0)
        }
    ReaderSettingDialogContent(
        state = state,
        currentSelectIndex = currentSelectIndex.value,
        onSelectItem = {
            currentSelectIndex.value = it
        },
    )
}

@Composable
fun ReaderSettingDialogContent(
    modifier: Modifier = Modifier,
    state: ReaderSettingState,
    currentSelectIndex: Int,
    onSelectItem: (Int) -> Unit = {},
) {
    Column(
        modifier,
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = currentSelectIndex,
        ) {
            TabRowItem.entries.forEach {
                Tab(
                    selected = currentSelectIndex == it.ordinal,
                    onClick = {
                        onSelectItem.invoke(it.ordinal)
                    },
                    text = {
                        Text(it.name)
                    },
                )
            }
        }
        when (TabRowItem.entries[currentSelectIndex]) {
            TabRowItem.FONT ->
                FontSetting(
                    fontSizeLevel = state.fontSizeLevel,
                    fontType = state.fontType,
                    onChangeSize = {
                        state.eventSink.invoke(ReaderSettingUiEvent.OnChangeFontSize(it))
                    },
                    onSelectedFontType = {
                        state.eventSink.invoke(ReaderSettingUiEvent.OnChangeFontType(it))
                    },
                )

            TabRowItem.LAYOUT -> {
                LayoutSetting(
                    topMargin = state.topMargin,
                    lineSpacing = state.lineSpacing,
                    theme = state.theme,
                    onSelectedTopMargin = {
                        state.eventSink.invoke(ReaderSettingUiEvent.OnChangeTopMargin(it))
                    },
                    onSelectedLineSpacing = {
                        state.eventSink.invoke(ReaderSettingUiEvent.OnChangeLineSpacing(it))
                    },
                    onSelectedTheme = {
                        state.eventSink.invoke(ReaderSettingUiEvent.OnChangeReaderTheme(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun LayoutSetting(
    modifier: Modifier = Modifier,
    topMargin: TopMargin,
    lineSpacing: LineSpacing,
    theme: ReaderTheme,
    onSelectedTopMargin: (TopMargin) -> Unit = {},
    onSelectedLineSpacing: (LineSpacing) -> Unit = {},
    onSelectedTheme: (ReaderTheme) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text("theme")

        Row {
            ReaderTheme.entries.forEach {
                ThemeItem(
                    theme = it,
                    isSelected = it == theme,
                    onClick = {
                        if (it != theme) {
                            onSelectedTheme(it)
                        }
                    },
                )
            }
        }

        HorizontalDivider()

        Text("line Spacing")

        Row {
            LineSpacing.entries.forEach {
                LineSpacingItem(
                    lineSpacing = it,
                    isSelected = it == lineSpacing,
                    onClick = {
                        if (it != lineSpacing) {
                            onSelectedLineSpacing(it)
                        }
                    },
                )
            }
        }
    }

    HorizontalDivider()

    Text("top margin")

    Row {
        TopMargin.entries.forEach {
            TopMarginItem(
                topMargin = it,
                isSelected = it == topMargin,
                onClick = {
                    if (it != topMargin) {
                        onSelectedTopMargin(it)
                    }
                },
            )
        }
    }
}

@Composable
private fun FontSetting(
    modifier: Modifier = Modifier,
    fontSizeLevel: FontSizeLevel,
    fontType: FontType,
    onChangeSize: (FontSizeLevel) -> Unit = {},
    onSelectedFontType: (FontType) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            FontType.entries.forEach {
                FontItem(
                    fontType = it,
                    isSelected = it == fontType,
                    onClick = {
                        if (it != fontType) {
                            onSelectedFontType(it)
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        AdjustFontSizeSlider(
            fontSizeLevel = fontSizeLevel,
            onChangeSize = onChangeSize,
        )
    }
}

@Composable
fun AdjustFontSizeSlider(
    modifier: Modifier = Modifier,
    fontSizeLevel: FontSizeLevel,
    onChangeSize: (FontSizeLevel) -> Unit = {},
) {
    Text(fontSizeLevel.name)
    Row(modifier = modifier) {
        TextButton(
            onClick = {
                onChangeSize.invoke(fontSizeLevel.pre())
            },
        ) {
            Text("A-")
        }
        TextButton(
            onClick = {
                onChangeSize.invoke(fontSizeLevel.next())
            },
        ) {
            Text("A+")
        }
    }
}

@Composable
private fun FontItem(
    modifier: Modifier = Modifier,
    fontType: FontType,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.size(72.dp),
        onClick = onClick,
    ) {
        Text(fontType.name + isSelected.toString())
    }
}

@Composable
private fun ThemeItem(
    modifier: Modifier = Modifier,
    theme: ReaderTheme,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.size(72.dp),
        onClick = onClick,
    ) {
        Text(theme.name + isSelected.toString())
    }
}

@Composable
private fun TopMarginItem(
    modifier: Modifier = Modifier,
    topMargin: TopMargin,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.size(72.dp),
        onClick = onClick,
    ) {
        Text(topMargin.name + isSelected.toString())
    }
}

@Composable
private fun LineSpacingItem(
    modifier: Modifier = Modifier,
    lineSpacing: LineSpacing,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.size(72.dp),
        onClick = onClick,
    ) {
        Text(lineSpacing.name + isSelected.toString())
    }
}

enum class TabRowItem {
    FONT,
    LAYOUT,
}

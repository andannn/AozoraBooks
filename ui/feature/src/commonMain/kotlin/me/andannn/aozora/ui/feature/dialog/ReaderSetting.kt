/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import me.andannn.aozora.core.domain.model.FontSizeLevel
import me.andannn.aozora.core.domain.model.FontType
import me.andannn.aozora.core.domain.model.LineSpacing
import me.andannn.aozora.core.domain.model.ReaderTheme
import me.andannn.aozora.core.domain.model.TopMargin
import me.andannn.aozora.core.domain.model.isLargest
import me.andannn.aozora.core.domain.model.isSmallest
import me.andannn.aozora.core.domain.model.next
import me.andannn.aozora.core.domain.model.pre
import me.andannn.aozora.core.domain.repository.UserDataRepository
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType
import me.andannn.aozora.ui.common.theme.getBackgroundColor
import me.andannn.aozora.ui.common.theme.getFontFamilyByType
import me.andannn.aozora.ui.common.theme.getTextColor
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
        modifier.fillMaxSize(),
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            selectedTabIndex = currentSelectIndex,
        ) {
            TabRowItem.entries.forEach {
                Tab(
                    selected = currentSelectIndex == it.ordinal,
                    onClick = {
                        onSelectItem.invoke(it.ordinal)
                    },
                    text = {
                        Text(it.label)
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
        Heading(text = "テーマ")

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
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

        Heading(text = "行間隔")

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
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

    Heading(text = "上部余白")

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
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
fun Heading(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier.padding(horizontal = 18.dp, vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium,
    )
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

        Spacer(modifier = Modifier.height(24.dp))

        Heading(text = "フォントサイズ")

        Spacer(modifier = Modifier.height(8.dp))

        AdjustFontSizeArea(
            fontSizeLevel = fontSizeLevel,
            onChangeSize = onChangeSize,
        )
    }
}

@Composable
fun AdjustFontSizeArea(
    modifier: Modifier = Modifier,
    fontSizeLevel: FontSizeLevel,
    onChangeSize: (FontSizeLevel) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            enabled = !fontSizeLevel.isSmallest(),
            onClick = {
                onChangeSize.invoke(fontSizeLevel.pre())
            },
        ) {
            Text("A-")
        }
        Text(fontSizeLevel.label)
        OutlinedButton(
            enabled = !fontSizeLevel.isLargest(),
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
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            onClick = onClick,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            shape = CircleShape,
            border =
                if (isSelected) {
                    BorderStroke(
                        3.dp,
                        MaterialTheme.colorScheme.outline,
                    )
                } else {
                    null
                },
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "あ",
                    fontSize = 32.sp,
                    fontFamily = getFontFamilyByType(fontType),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            text = fontType.label,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun ThemeItem(
    modifier: Modifier = Modifier,
    theme: ReaderTheme,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val border =
        BorderStroke(3.dp, MaterialTheme.colorScheme.outline).takeIf { isSelected }
    val colorScheme = MaterialTheme.colorScheme
    val bgColor = theme.getBackgroundColor(colorScheme)
    val textColor = theme.getTextColor(colorScheme)
    Column(
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = bgColor,
            border = border,
            onClick = onClick,
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "あ",
                    fontSize = 32.sp,
                    color = textColor,
                )
            }
        }
    }
}

@Composable
private fun TopMarginItem(
    modifier: Modifier = Modifier,
    topMargin: TopMargin,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    SelectedButton(
        modifier = modifier,
        onClick = onClick,
        text = topMargin.label,
        selected = isSelected,
    )
}

@Composable
private fun LineSpacingItem(
    modifier: Modifier = Modifier,
    lineSpacing: LineSpacing,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    SelectedButton(
        modifier = modifier,
        onClick = onClick,
        text = lineSpacing.label,
        selected = isSelected,
    )
}

@Composable
private fun SelectedButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Text(text)
    }
}

enum class TabRowItem {
    FONT,
    LAYOUT,
}

val TabRowItem.label
    get() =
        when (this) {
            TabRowItem.FONT -> "フォント"
            TabRowItem.LAYOUT -> "レイアウト"
        }

val FontType.label
    get() =
        when (this) {
            FontType.NOTO_SANS -> "システム"
            FontType.NOTO_SERIF -> "Noto Serif Japanese"
        }

val FontSizeLevel.label
    get() = "レベル　${this.ordinal + 1}"

val LineSpacing.label
    get() =
        when (this) {
            LineSpacing.SMALL -> "小"
            LineSpacing.MEDIUM -> "中"
            LineSpacing.LARGE -> "大"
        }
val TopMargin.label
    get() =
        when (this) {
            TopMargin.SMALL -> "小"
            TopMargin.MEDIUM -> "中"
            TopMargin.LARGE -> "大"
        }

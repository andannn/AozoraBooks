package me.andannn.aozora.ui.feature.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import me.andannn.aozora.core.data.UserDataRepository
import me.andannn.aozora.ui.common.dialog.DialogAction
import me.andannn.aozora.ui.common.dialog.DialogId
import me.andannn.aozora.ui.common.dialog.DialogType
import org.koin.mp.KoinPlatform.getKoin

object TableOfContentsDialogId : DialogId {
    override val dialogType: DialogType = DialogType.ModalBottomSheet

    @Composable
    override fun Content(onAction: (DialogAction) -> Unit) {
        TableOfContentsDialog()
    }
}

@Composable
fun rememberTableOfContentsDialogPresenter(settingRepository: UserDataRepository = getKoin().get()) =
    remember(settingRepository) {
        ReaderSettingDialogPresenter(settingRepository)
    }

@Composable
fun TableOfContentsDialog() {
}

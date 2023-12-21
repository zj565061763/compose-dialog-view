package com.sd.lib.compose.dialogview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.vdialog.IDialog

object DialogViewHook {
    /** [setComposable]拦截 */
    var contentHook: @Composable ((@Composable () -> Unit)) -> Unit = { content ->
        content()
    }

    /** [FDialogConfirmView]拦截 */
    var dialogConfirmViewHook: ((FDialogConfirmViewParams) -> FDialogConfirmViewParams) = { it }
}

/**
 * 设置内容
 */
fun IDialog.setComposable(content: @Composable () -> Unit) {
    val view = contentView
    val composeView = if (view is ComposeView) {
        view
    } else {
        ComposeView(context).also {
            setContentView(it)
        }
    }

    composeView.setContent {
        val hook = DialogViewHook.contentHook
        hook(content)
    }
}

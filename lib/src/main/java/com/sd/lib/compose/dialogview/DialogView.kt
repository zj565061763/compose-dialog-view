package com.sd.lib.compose.dialogview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.vdialog.IDialog

object DialogViewHook {
    /** 确认View参数拦截 */
    var confirmViewParamsHook: ((FDialogConfirmViewParams) -> Unit)? = null

    /** [setComposable]拦截 */
    var setComposableHook: (@Composable (content: (@Composable () -> Unit), dialog: IDialog) -> Unit)? = null
}

/**
 * 设置内容
 */
fun IDialog.setComposable(content: @Composable (IDialog) -> Unit) {
    val view = contentView
    val composeView = if (view is ComposeView) {
        view
    } else {
        ComposeView(context).also {
            setContentView(it)
        }
    }

    composeView.setContent {
        val hook = DialogViewHook.setComposableHook
        if (hook == null) {
            content(this@setComposable)
        } else {
            hook({ content(this@setComposable) }, this@setComposable)
        }
    }
}

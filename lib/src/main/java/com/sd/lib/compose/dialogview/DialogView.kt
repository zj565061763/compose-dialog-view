package com.sd.lib.compose.dialogview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.vdialog.IDialog

object DialogHook {
    /** 拦截[setComposable]的内容 */
    var hook: @Composable ((@Composable () -> Unit)) -> Unit = { content ->
        content()
    }

    /** 拦截[FDialogConfirmView]参数 */
    var confirmViewParamsHook: ((FDialogConfirmViewParams) -> FDialogConfirmViewParams) = { it }
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
        DialogHook.hook {
            content()
        }
    }
}

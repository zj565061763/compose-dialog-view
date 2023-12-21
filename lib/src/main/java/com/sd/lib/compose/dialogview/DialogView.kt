package com.sd.lib.compose.dialogview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.activity.fLastActivity
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog

object DialogViewHook {
    /** 拦截[setComposable]的内容 */
    var hook: @Composable ((@Composable () -> Unit)) -> Unit = { it() }

    /** 拦截[FDialogConfirmView]参数 */
    var confirmViewParamsHook: ((FDialogConfirmViewParams) -> FDialogConfirmViewParams) = { it }
}

/**
 * 设置内容
 */
fun IDialog.setComposable(content: @Composable IDialog.() -> Unit) {
    val view = contentView
    val composeView = if (view is ComposeView) {
        view
    } else {
        ComposeView(context).also {
            setContentView(it)
        }
    }

    composeView.setContent {
        DialogViewHook.hook {
            content()
        }
    }
}

fun fDialogCompose(content: @Composable IDialog.() -> Unit): IDialog? = fDialog()?.apply {
    setComposable(content)
}

fun fDialog(): IDialog? = fLastActivity?.let { FDialog(it) }
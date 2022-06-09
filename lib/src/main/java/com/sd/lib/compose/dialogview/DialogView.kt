package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.impl.FDialog

/**
 * 窗口
 */
@Composable
fun rememberFDialog(apply: IDialog.() -> Unit): IDialog {
    val context = LocalContext.current
    val dialog: IDialog = remember {
        FDialog(context as Activity).apply {
            setContentView(ComposeView(context))
            apply(this)
        }
    }
    DisposableEffect(dialog) {
        onDispose {
            dialog.dismiss()
        }
    }
    return dialog
}

/**
 * 设置内容
 */
fun IDialog.setComposable(content: @Composable (IDialog) -> Unit) {
    var view = contentView
    if (view !is ComposeView) {
        view = ComposeView(context).also {
            setContentView(it)
        }
    }
    view.setContent {
        val hook = DialogViewHook.setComposableHook
        if (hook == null) {
            content(this@setComposable)
        } else {
            hook(content, this@setComposable)
        }
    }
}

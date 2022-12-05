package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog

/**
 * 窗口
 */
@Composable
fun rememberFDialog(
    dismissOnDispose: Boolean = true,
    apply: IDialog.() -> Unit,
): IDialog {
    val dismissOnDisposeUpdated by rememberUpdatedState(dismissOnDispose)

    val context = LocalContext.current
    val dialog = remember(context) {
        FDialog(context as Activity).apply {
            apply(this)
        }
    }

    DisposableEffect(dialog) {
        onDispose {
            if (dismissOnDisposeUpdated) {
                dialog.dismiss()
            }
        }
    }
    return dialog
}

/**
 * 确认窗口
 */
@Composable
fun rememberFDialogConfirm(
    dismissOnDispose: Boolean = true,
    apply: FDialogConfirm.() -> Unit
): FDialogConfirm {
    val dismissOnDisposeUpdated by rememberUpdatedState(dismissOnDispose)

    val context = LocalContext.current
    val dialog = remember(context) {
        FDialogConfirm(context as Activity).apply {
            apply(this)
        }
    }

    DisposableEffect(dialog) {
        onDispose {
            if (dismissOnDisposeUpdated) {
                dialog.dismiss()
            }
        }
    }
    return dialog
}

/**
 * 确认窗口
 */
@Composable
fun <T> rememberFDialogMenu(
    dismissOnDispose: Boolean = true,
    apply: FDialogMenu<T>.() -> Unit
): FDialogMenu<T> {
    val dismissOnDisposeUpdated by rememberUpdatedState(dismissOnDispose)

    val context = LocalContext.current
    val dialog = remember(context) {
        FDialogMenu<T>(context as Activity).apply {
            apply(this)
        }
    }

    DisposableEffect(dialog) {
        onDispose {
            if (dismissOnDisposeUpdated) {
                dialog.dismiss()
            }
        }
    }
    return dialog
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

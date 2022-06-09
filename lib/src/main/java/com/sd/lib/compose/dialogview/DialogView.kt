package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.impl.FDialog

fun IDialog.setContent(content: @Composable (IDialog) -> Unit) {
    val view = contentView
    if (view is ComposeView) {
        view.setContent { content(this) }
    } else {
        ComposeView(context).also {
            setContentView(it)
            it.setContent { content(this) }
        }
    }
}

/**
 * 窗口
 */
@Composable
fun rememberFDialog(
    apply: (IDialog.() -> Unit)? = null,
    content: @Composable (IDialog) -> Unit,
): IDialog {
    val context = LocalContext.current
    val dialog: IDialog = remember {
        FDialog(context as Activity).apply {
            setContentView(ComposeView(context))
            apply?.invoke(this)
        }
    }.apply {
        (contentView as ComposeView).let {
            it.setContent {
                content(this@apply)
            }
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
 * 下拉动画的窗口
 */
@Composable
fun rememberFDialogFillWidth(
    apply: (IDialog.() -> Unit)? = null,
    content: @Composable (IDialog) -> Unit,
): IDialog {
    return rememberFDialog(
        apply = {
            setPadding(0, 0, 0, 0)
            apply?.invoke(this)
        },
        content = content,
    )
}
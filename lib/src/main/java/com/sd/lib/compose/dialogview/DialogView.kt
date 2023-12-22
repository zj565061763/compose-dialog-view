package com.sd.lib.compose.dialogview

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.activity.fLastActivity
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog

object DialogViewHook {
    /** 拦截[setComposable]的内容 */
    var hook: @Composable ((@Composable () -> Unit)) -> Unit = { it() }
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

fun fDialog(
    context: Context? = fLastActivity,
    content: @Composable (IDialog.() -> Unit)? = null,
): IDialog? {
    return context?.let { FDialog(it) }?.apply {
        if (content != null) {
            setComposable(content)
        }
    }
}
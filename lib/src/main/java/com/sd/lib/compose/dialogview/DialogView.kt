package com.sd.lib.compose.dialogview

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.activity.fLastActivity
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

/**
 * 确认窗口
 */
fun fDialogConfirm(
    context: Context? = fLastActivity,
    block: FDialogConfirm.() -> Unit,
): FDialogConfirm? {
    return context?.let {
        FDialogConfirm(it).also(block)
    }
}

/**
 * 菜单窗口
 */
fun <T> fDialogMenu(
    context: Context? = fLastActivity,
    block: FDialogMenu<T>.() -> Unit,
): FDialogMenu<T>? {
    return context?.let {
        FDialogMenu<T>(it).also(block)
    }
}

/**
 * 加载窗口
 */
fun fDialogLoading(
    context: Context? = fLastActivity,
    block: FDialogLoading.() -> Unit,
): FDialogLoading? {
    return context?.let {
        FDialogLoading(it).also(block)
    }
}
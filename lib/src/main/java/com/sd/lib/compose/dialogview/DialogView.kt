package com.sd.lib.compose.dialogview

import android.content.Context
import android.view.Gravity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog
import com.sd.lib.vdialog.animator.slide.SlideUpDownRItselfFactory

object DialogView {
    /**
     * 确认窗口
     */
    fun confirm(context: Context, content: @Composable IDialog.() -> Unit): IDialog {
        return FDialog(context).apply {
            setCanceledOnTouchOutside(false)
            setComposable(content)
        }
    }

    /**
     * 菜单窗口
     */
    fun menu(context: Context, content: @Composable IDialog.() -> Unit): IDialog {
        return FDialog(context).apply {
            padding.set(0, 0, 0, 0)
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            animatorFactory = SlideUpDownRItselfFactory()
            setCanceledOnTouchOutside(true)
            setComposable(content)
        }
    }

    /**
     * 加载窗口
     */
    fun progress(context: Context, content: @Composable IDialog.() -> Unit): IDialog {
        return FDialog(context).apply {
            padding.set(0, 0, 0, 0)
            setCanceledOnTouchOutside(false)
            setComposable(content)
        }
    }
}

object DialogViewHook {
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

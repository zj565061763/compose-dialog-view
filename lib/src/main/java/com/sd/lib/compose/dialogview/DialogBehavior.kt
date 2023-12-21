package com.sd.lib.compose.dialogview

import android.view.Gravity
import com.sd.lib.vdialog.IDialog
import com.sd.lib.vdialog.animator.slide.SlideUpDownRItselfFactory

/**
 * 窗口行为
 */
object DialogBehavior {
    internal var confirm: IDialog.() -> Unit = {
        setCanceledOnTouchOutside(false)
    }

    internal var menu: IDialog.() -> Unit = {
        padding.set(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorFactory = SlideUpDownRItselfFactory()
        setCanceledOnTouchOutside(true)
    }

    internal var progress: IDialog.() -> Unit = {
        padding.set(0, 0, 0, 0)
        setCanceledOnTouchOutside(false)
    }

    /**
     * 配置确认窗口行为
     */
    @JvmStatic
    fun confirm(block: IDialog.() -> Unit) {
        this.confirm = block
    }

    /**
     * 配置菜单窗口行为
     */
    @JvmStatic
    fun menu(block: IDialog.() -> Unit) {
        this.menu = block
    }

    /**
     * 配置加载窗口行为
     */
    @JvmStatic
    fun progress(block: IDialog.() -> Unit) {
        this.progress = block
    }
}

/**
 * 确认窗口行为
 */
fun IDialog?.beConfirm(show: Boolean = true): IDialog? = this?.apply {
    DialogBehavior.confirm(this)
    if (show) show()
}

/**
 * 菜单窗口行为
 */
fun IDialog?.beMenu(show: Boolean = true): IDialog? = this?.apply {
    DialogBehavior.menu(this)
    if (show) show()
}

/**
 * 加载窗口行为
 */
fun IDialog?.beProgress(show: Boolean = true): IDialog? = this?.apply {
    DialogBehavior.progress(this)
    if (show) show()
}
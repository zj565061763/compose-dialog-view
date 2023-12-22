package com.sd.lib.compose.dialogview

import android.view.Gravity
import com.sd.lib.vdialog.IDialog
import com.sd.lib.vdialog.animator.slide.SlideUpDownRItselfFactory

/**
 * 窗口行为
 */
object DialogBehavior {
    internal var confirm: (FDialogConfirm.() -> Unit)? = null
        private set

    internal var menu: IDialog.() -> Unit = {
        padding.set(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorFactory = SlideUpDownRItselfFactory()
        setCanceledOnTouchOutside(true)
    }

    internal var progress: (FDialogProgress.() -> Unit)? = null
        private set

    /**
     * 配置确认窗口行为
     */
    @JvmStatic
    fun confirm(block: FDialogConfirm.() -> Unit) {
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
    fun progress(block: FDialogProgress.() -> Unit) {
        this.progress = block
    }
}
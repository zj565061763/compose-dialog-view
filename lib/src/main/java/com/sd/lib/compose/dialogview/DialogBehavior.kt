package com.sd.lib.compose.dialogview

import android.view.Gravity
import com.sd.lib.vdialog.IDialog
import com.sd.lib.vdialog.animator.slide.SlideUpDownRItselfFactory

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

    @JvmStatic
    fun confirm(block: IDialog.() -> Unit) {
        this.confirm = block
    }

    @JvmStatic
    fun menu(block: IDialog.() -> Unit) {
        this.menu = block
    }

    @JvmStatic
    fun progress(block: IDialog.() -> Unit) {
        this.progress = block
    }
}

fun IDialog?.beConfirm(show: Boolean = true): IDialog? = this?.apply {
    DialogBehavior.confirm(this)
    if (show) show()
}

fun IDialog?.beMenu(show: Boolean = true): IDialog? = this?.apply {
    DialogBehavior.menu(this)
    if (show) show()
}

fun IDialog?.beProgress(show: Boolean = true): IDialog? = this?.apply {
    DialogBehavior.progress(this)
    if (show) show()
}
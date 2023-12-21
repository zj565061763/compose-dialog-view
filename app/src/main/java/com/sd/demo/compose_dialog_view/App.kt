package com.sd.demo.compose_dialog_view

import android.app.Application
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.DialogBehavior
import com.sd.lib.compose.dialogview.DialogViewHook
import com.sd.lib.vdialog.animator.scale.ScaleXYFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 拦截
        DialogViewHook.hook = { content ->
            AppTheme {
                content()
            }
        }

        // 配置确认窗口行为
        DialogBehavior.confirm {
            this.animatorFactory = ScaleXYFactory()
        }
    }
}
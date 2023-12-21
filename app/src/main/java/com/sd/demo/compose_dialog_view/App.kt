package com.sd.demo.compose_dialog_view

import android.app.Application
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.DialogViewHook

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DialogViewHook.setComposableHook = { content, _ ->
            AppTheme {
                content()
            }
        }
    }
}
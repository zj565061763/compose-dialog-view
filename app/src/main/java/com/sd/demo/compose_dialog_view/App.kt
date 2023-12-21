package com.sd.demo.compose_dialog_view

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.DialogConfirmViewHook
import com.sd.lib.compose.dialogview.DialogHook

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DialogHook.hook = { content ->
            AppTheme {
                content()
            }
        }
    }
}

private fun hookConfirmViewButton() {
    DialogConfirmViewHook.paramsHook = { params ->
        val cancel = params.cancel
        val confirm = params.confirm
        if (cancel != null && confirm != null) {
            params.copy(
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Button(onClick = { params.onClickCancel?.invoke() }) {
                            cancel()
                        }
                        Button(onClick = { params.onClickConfirm?.invoke() }) {
                            confirm()
                        }
                    }
                }
            )
        } else {
            params
        }
    }
}

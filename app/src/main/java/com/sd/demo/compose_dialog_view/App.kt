package com.sd.demo.compose_dialog_view

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.DialogHook
import com.sd.lib.compose.dialogview.DialogHookConfirmView
import com.sd.lib.compose.dialogview.FDialogConfirmViewShapes
import com.sd.lib.compose.dialogview.LocalFDialogConfirmViewShapes

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DialogHook.hook = { content ->
            AppTheme {
                content()
            }
        }

//        hookConfirmView()
//        hookConfirmViewButton()
    }
}

private fun hookConfirmView() {
    DialogHookConfirmView.hook = { content ->
        val customShape = remember {
            FDialogConfirmViewShapes(dialog = CircleShape)
        }
        CompositionLocalProvider(
            LocalFDialogConfirmViewShapes provides customShape
        ) {
            content()
        }
    }
}

private fun hookConfirmViewButton() {
    DialogHookConfirmView.paramsHook = { params ->
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

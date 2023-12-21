package com.sd.demo.compose_dialog_view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.DialogViewHook
import com.sd.lib.compose.dialogview.FDialogConfirm
import com.sd.lib.compose.dialogview.FDialogConfirmViewColors
import com.sd.lib.compose.dialogview.FDialogConfirmViewDefaults
import com.sd.lib.compose.dialogview.FDialogMenu
import com.sd.lib.compose.dialogview.FDialogMenuViewColors
import com.sd.lib.compose.dialogview.FDialogMenuViewDefaults
import com.sd.lib.compose.dialogview.FDialogProgress
import com.sd.lib.compose.dialogview.setComposable
import com.sd.lib.vdialog.animator.scale.ScaleXYFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainView()
                }
            }
        }
    }

    companion object {
        init {
//            testHook()
        }
    }
}

/**
 * 拦截
 */
private fun testHook() {
    // 确认View参数拦截
    DialogViewHook.confirmViewParamsHook = {
        val cancel = it.cancel
        val confirm = it.confirm
        if (cancel != null || confirm != null) {
            it.buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    if (cancel != null) {
                        Button(onClick = { it.onClickCancel?.invoke() }) {
                            cancel.invoke()
                        }
                    }
                    if (confirm != null) {
                        Button(onClick = { it.onClickConfirm?.invoke() }) {
                            confirm.invoke()
                        }
                    }
                }
            }
        }
    }

    /** [setComposable]拦截 */
    DialogViewHook.setComposableHook = { content, dialog ->
        AppTheme {
            Column {
                Text(text = "setComposableHook")
                content()
            }
        }
    }
}

@Composable
fun MainView() {
    val context = LocalContext.current as Activity

    var isLight by remember { mutableStateOf(true) }
    LaunchedEffect(isLight) {
        if (isLight) {
            FDialogConfirmViewDefaults.colors = FDialogConfirmViewColors.light()
            FDialogMenuViewDefaults.colors = FDialogMenuViewColors.light()
        } else {
            FDialogConfirmViewDefaults.colors = FDialogConfirmViewColors.dark()
            FDialogMenuViewDefaults.colors = FDialogMenuViewColors.dark()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Light or Dark
        Button(
            onClick = {
                isLight = isLight.not()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = if (isLight) "Light" else "Dark")
        }

        // Confirm
        Button(
            onClick = {
                showConfirmDialog(context)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Confirm")
        }

        // Menu
        Button(
            onClick = {
                showMenuDialog(context)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Menu")
        }

        // Progress
        Button(
            onClick = {
                FDialogProgress(context).apply {
                    setTextMsg("加载中")
                }.show()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Progress")
        }
    }
}

/**
 * 确认窗口
 */
private fun showConfirmDialog(context: Context) {
    val dialog = FDialogConfirm(context).apply {
        animatorFactory = ScaleXYFactory()

        setTitle { Text(text = "Title") }
        setContent { Text(text = "Content") }
        setCancel { Text(text = "Cancel") }
        setConfirm { Text(text = "Confirm") }

        onClickCancel {
            Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        onClickConfirm {
            Toast.makeText(context, "onConfirm", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
    dialog.show()
}

/**
 * 菜单窗口
 */
private fun showMenuDialog(context: Context) {
    val list = listOf(
        "Kotlin",
        "Java",
        "C",
        "C++",
        "OC",
        "Swift",
        "PHP",
        "C#",
        "GO",
        "Groovy",
        "Rust",
        "Python",
        "Javascript",
        "Html",
        "CSS",
    )

    val dialog = FDialogMenu<String>(context).apply {
        setTextTitle("Title")
        setTextCancel("Cancel")

        data.addAll(list)

        // 自定义每一行的样式
//        setRow { index, item ->
//            Text(text = "I'm row $item")
//        }

        onClickCancel {
            dismiss()
            Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
        }
        onClickRow { index, item, dialog ->
            dismiss()
            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
        }
    }
    dialog.show()
}
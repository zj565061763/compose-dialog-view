package com.sd.demo.compose_dialog_view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.*
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
    val activity = LocalContext.current as Activity

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
                showConfirmDialog(activity)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Confirm")
        }

        // Menu
        Button(
            onClick = {
                showMenuDialog(activity)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Menu")
        }

        // Progress
        Button(
            onClick = {
                showProgressDialog(activity, "加载中")
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
private fun showConfirmDialog(activity: Activity) {
    val dialog = FDialogConfirm(activity).apply {
        animatorFactory = ScaleXYFactory()

        setTextTitle("Title")
        setTextContent("Content")
        setTextCancel("Cancel")
        setTextConfirm("Confirm")

        onClickCancel = {
            Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        onClickConfirm = {
            Toast.makeText(activity, "onConfirm", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
    dialog.show()
}

/**
 * 菜单窗口
 */
private fun showMenuDialog(activity: Activity) {
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

    val dialog = FDialogMenu<String>(activity).apply {
        title = "title"
        cancel = "cancel"
        data.addAll(list)

        // 自定义每一行的样式
//        row = { index, item ->
//            Text(text = "I'm row $item")
//        }

        onClickCancel = {
            dismiss()
            Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show()
        }
        onClickRow = { index, item, _ ->
            dismiss()
            Toast.makeText(activity, item, Toast.LENGTH_SHORT).show()
        }
    }
    dialog.show()
}

private fun showProgressDialog(activity: Activity, text: String) {
    FDialogProgress(activity).apply {
        this.text = text
    }.show()
}
package com.sd.demo.compose_dialog_view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.FDialogConfirmView
import com.sd.lib.compose.dialogview.FDialogConfirmViewColors
import com.sd.lib.compose.dialogview.FDialogConfirmViewDefaults
import com.sd.lib.compose.dialogview.FDialogMenu
import com.sd.lib.compose.dialogview.FDialogMenuViewColors
import com.sd.lib.compose.dialogview.FDialogMenuViewDefaults
import com.sd.lib.compose.dialogview.FDialogProgress
import com.sd.lib.compose.dialogview.setComposable
import com.sd.lib.vdialog.FDialog
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
    FDialog(context).apply {
        // 设置动画
        animatorFactory = ScaleXYFactory()

        this.setComposable {
            FDialogConfirmView(
                title = { Text(text = "Title") },
                cancel = { Text(text = "Cancel") },
                confirm = { Text(text = "Confirm") },
                onClickCancel = {
                    Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
                    dismiss()
                },
                onClickConfirm = {
                    Toast.makeText(context, "onConfirm", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            ) {
                Text(text = "Content")
            }
        }
    }.show()
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
        setTitle { Text(text = "title") }
        setCancel { Text(text = "Cancel") }

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
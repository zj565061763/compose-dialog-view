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
import com.sd.lib.compose.dialogview.FDialogMenuView
import com.sd.lib.compose.dialogview.FDialogMenuViewColors
import com.sd.lib.compose.dialogview.FDialogMenuViewDefaults
import com.sd.lib.compose.dialogview.FDialogProgressView
import com.sd.lib.compose.dialogview.beConfirm
import com.sd.lib.compose.dialogview.beMenu
import com.sd.lib.compose.dialogview.beProgress
import com.sd.lib.compose.dialogview.fDialogCompose

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
                showProgressDialog(context)
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
    fDialogCompose {
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
    }.beConfirm()
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

    fDialogCompose {
        FDialogMenuView(
            data = list,
            onClickCancel = {
                dismiss()
                Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
            },
            onClickRow = { _, item ->
                dismiss()
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
            },
            text = { _, item ->
                item
            }
        )
    }.beMenu()
}

/**
 * 加载窗口
 */
private fun showProgressDialog(context: Context) {
    fDialogCompose {
        FDialogProgressView {
            Text(text = "加载中")
        }
    }.beProgress()
}
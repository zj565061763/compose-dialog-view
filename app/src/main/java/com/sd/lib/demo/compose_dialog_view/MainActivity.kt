package com.sd.lib.demo.compose_dialog_view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.sd.lib.compose.dialogview.*
import com.sd.lib.demo.compose_dialog_view.ui.theme.ComposedialogviewTheme
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.animator.ScaleXYCreator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FDialogConfirmViewDefaults.colors = FDialogConfirmViewColors.light()

        setContent {
            ComposedialogviewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView() {
    val confirmDialog = confirmDialog()
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Confirm
        Button(
            onClick = {
                showConfirmDialog(activity)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Confirm")
        }

        // Confirm in Composable
        Button(
            onClick = {
                confirmDialog.show()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Confirm in Composable")
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
    }
}

@Composable
private fun confirmDialog(): IDialog {
    val context = LocalContext.current
    return rememberFDialog {
        // 设置动画类型
        animatorCreator = ScaleXYCreator()
        // 设置显示内容
        setComposable {
            FDialogConfirmView(
                onClickCancel = {
                    Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
                    dismiss()
                },
                onClickConfirm = {
                    Toast.makeText(context, "onConfirm", Toast.LENGTH_SHORT).show()
                    dismiss()
                },
            ) {
                Text(text = "Confirm in Composable")
            }
        }
    }
}

/**
 * 确认窗口
 */
private fun showConfirmDialog(activity: Activity) {
    val dialog = FDialogConfirm(activity).apply {
        animatorCreator = ScaleXYCreator()

        title = "title"
        content = "content"
        cancel = "cancel"
        confirm = "confirm"

        onClickCancel = {
            Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show()
            it.dismiss()
        }
        onClickConfirm = {
            Toast.makeText(activity, "onConfirm", Toast.LENGTH_SHORT).show()
            it.dismiss()
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
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
        "Javascript",
    )

    val dialog = FDialogMenu<String>(activity).apply {
        title = "title"
        cancel = "cancel"
        data.addAll(list)
    }

    dialog.onClickRow = { index, item, _ ->
        dialog.dismiss()
        Toast.makeText(activity, item, Toast.LENGTH_SHORT).show()
    }
    dialog.onClickCancel = {
        dialog.dismiss()
        Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show()
    }
    dialog.show()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposedialogviewTheme {
    }
}
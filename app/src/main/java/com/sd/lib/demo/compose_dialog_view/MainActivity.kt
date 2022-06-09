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
import com.sd.lib.compose.dialogview.FDialogConfirm
import com.sd.lib.compose.dialogview.FDialogConfirmViewColors
import com.sd.lib.compose.dialogview.FDialogConfirmViewDefaults
import com.sd.lib.compose.dialogview.FDialogMenu
import com.sd.lib.demo.compose_dialog_view.ui.theme.ComposedialogviewTheme
import com.sd.lib.dialog.animator.ScaleXYCreator

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FDialogConfirmViewDefaults.colors = FDialogConfirmViewColors.light()

        // 参数拦截
        FDialogConfirmViewDefaults.paramsHook = {
            it.title = {
                Text(text = "我是拦截的title")
            }
        }

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

/**
 * 确认窗口
 */
private fun showConfirmDialog(activity: Activity) {
    val dialog = FDialogConfirm(activity).apply {
        animatorCreator = ScaleXYCreator()

        title = "title"

        content = "我是 content"
        composableContent = {
            Text(text = "我是 content 我的优先级更高")
        }

        cancel = "cancel"
        confirm = "confirm"

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposedialogviewTheme {
    }
}
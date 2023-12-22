package com.sd.demo.compose_dialog_view

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sd.demo.compose_dialog_view.ui.theme.AppTheme
import com.sd.lib.compose.dialogview.FDialogConfirmViewColors
import com.sd.lib.compose.dialogview.FDialogConfirmViewDefaults
import com.sd.lib.compose.dialogview.FDialogMenuViewColors
import com.sd.lib.compose.dialogview.FDialogMenuViewDefaults
import com.sd.lib.compose.dialogview.fDialogConfirm
import com.sd.lib.compose.dialogview.fDialogLoading
import com.sd.lib.compose.dialogview.fDialogMenu

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
    Column(modifier = Modifier.fillMaxWidth()) {
        // Light or Dark
        var isLight by lightState()
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isLight = isLight.not() },
        ) {
            Text(text = if (isLight) "Light" else "Dark")
        }

        // Loading
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDialogLoading(isLight) },
        ) {
            Text(text = "Loading")
        }

        // Confirm
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDialogConfirm() },
        ) {
            Text(text = "Confirm")
        }

        // Menu
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDialogMenu() },
        ) {
            Text(text = "Menu")
        }
    }
}

@Composable
private fun lightState(): MutableState<Boolean> {
    val lightState = remember { mutableStateOf(true) }
    LaunchedEffect(lightState.value) {
        if (lightState.value) {
            FDialogConfirmViewDefaults.colors = FDialogConfirmViewColors.light()
            FDialogMenuViewDefaults.colors = FDialogMenuViewColors.light()
        } else {
            FDialogConfirmViewDefaults.colors = FDialogConfirmViewColors.dark()
            FDialogMenuViewDefaults.colors = FDialogMenuViewColors.dark()
        }
    }
    return lightState
}

/**
 * 加载窗口
 */
private fun showDialogLoading(isLight: Boolean) {
    fDialogLoading {
        this.hook = { content ->
            AppTheme(isLight = isLight) {
                content()
            }
        }
        this.text = { Text(text = "加载中") }
        this.show()
    }
}

/**
 * 确认窗口
 */
private fun showDialogConfirm() {
    fDialogConfirm {
        this.title = { Text(text = "Title") }
        this.content = { Text(text = "Content") }
        this.cancel = { Text(text = "Cancel") }
        this.confirm = { Text(text = "Confirm") }

        this.onClickCancel = {
            Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        this.onClickConfirm = {
            Toast.makeText(context, "onConfirm", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        this.show()
    }
}

/**
 * 菜单窗口
 */
private fun showDialogMenu() {
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

    fDialogMenu<String> {
        this.data = list
        this.title = { Text(text = "Select Language") }

        this.onClickCancel = {
            dismiss()
            Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
        }
        this.onClickRow = { index, item, _ ->
            dismiss()
            Toast.makeText(context, "$index -> $item", Toast.LENGTH_SHORT).show()
        }
        this.show()
    }
}
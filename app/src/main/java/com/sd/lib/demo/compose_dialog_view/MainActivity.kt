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
    }
}

@Composable
private fun confirmDialog(): IDialog {
    val context = LocalContext.current
    return rememberFDialog(
        apply = {
            animatorCreator = ScaleXYCreator()
        }
    ) { dialog ->
        FDialogConfirmView(
            onCancel = {
                Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            },
            onConfirm = {
                Toast.makeText(context, "onConfirm", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            },
        ) {
            Text(text = "Confirm in Composable")
        }
    }
}

private fun showConfirmDialog(activity: Activity) {
    fDialogConfirm(
        activity = activity,
        content = "Confirm",
        onCancel = {
            Toast.makeText(activity, "onCancel", Toast.LENGTH_SHORT).show()
            it.dismiss()
        },
        onConfirm = {
            Toast.makeText(activity, "onConfirm", Toast.LENGTH_SHORT).show()
            it.dismiss()
        }
    ).apply { animatorCreator = ScaleXYCreator() }.show()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposedialogviewTheme {
    }
}
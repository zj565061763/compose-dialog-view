package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.lib.vdialog.FDialog

class FDialogProgress(activity: Activity) : FDialog(activity) {
    /** 提示文字 */
    var text by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 自定义进度框 */
    var progress by mutableStateOf<@Composable (() -> Unit)?>(null)

    override fun onCreate() {
        super.onCreate()
        setComposable {
            FDialogProgressView(
                text = text,
                progress = progress,
            )
        }
    }

    /**
     * 设置文字
     */
    fun setText(text: String?) {
        this.text = if (text == null) {
            null
        } else {
            { Text(text = text) }
        }
    }

    init {
        padding.set(0, 0, 0, 0)
        setCanceledOnTouchOutside(false)
    }
}

@Composable
fun FDialogProgressView(
    modifier: Modifier = Modifier,
    text: (@Composable () -> Unit)? = null,
    progress: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(color = Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (progress != null) {
            progress()
        } else {
            CircularProgressIndicator(
                modifier = modifier.size(16.dp),
                color = Color.White.copy(alpha = 0.9f),
                strokeWidth = 2.dp,
            )
        }

        if (text != null) {
            Spacer(modifier = Modifier.width(5.dp))
            ProvideTextStyle(TextStyle(color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)) {
                text()
            }
        }
    }
}
package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
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
    /** 文字 */
    internal var msg by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 进度框 */
    internal var progress by mutableStateOf<@Composable (() -> Unit)?>(null)

    override fun onCreate() {
        super.onCreate()
        setComposable {
            FDialogProgressView(
                msg = msg,
                progress = progress,
            )
        }
    }

    /**
     * 设置文字
     */
    fun setTextMsg(text: String?) {
        this.msg = if (text == null) {
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

/**
 * 设置文字
 */
fun FDialogProgress.setMsg(block: @Composable (() -> Unit)?) {
    this.msg = block
}

/**
 * 设置进度框
 */
fun FDialogProgress.setProgress(block: @Composable (() -> Unit)?) {
    this.progress = block
}

@Composable
fun FDialogProgressView(
    modifier: Modifier = Modifier,
    msg: (@Composable () -> Unit)? = null,
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

        if (msg != null) {
            Spacer(modifier = Modifier.width(5.dp))
            ProvideTextStyle(TextStyle(color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)) {
                msg()
            }
        }
    }
}
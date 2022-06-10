package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
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
import com.sd.lib.dialog.impl.FDialog

class FDialogProgress(activity: Activity) : FDialog(activity) {
    /** 提示文字 */
    var text by mutableStateOf<String?>(null)
    /** 自定义提示文字，优先级高于[text] */
    var composableText by mutableStateOf<@Composable (() -> Unit)?>(null)
    /** 自定义进度框 */
    var composableProgress by mutableStateOf<@Composable (() -> Unit)?>(null)

    override fun onCreate() {
        super.onCreate()
        setComposable {
            val text = text
            FDialogProgressView(
                text = composableText ?: if (text.isNullOrEmpty()) null else {
                    { Text(text = text) }
                },
                progress = composableProgress,
            )
        }
    }

    init {
        setPadding(0, 0, 0, 0)
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
            .background(color = Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (progress != null) {
            progress()
        } else {
            CircularProgressIndicator(
                modifier = modifier.size(15.dp),
                color = MaterialTheme.colors.primary,
                strokeWidth = 2.dp,
            )
        }

        ProvideTextStyle(TextStyle(color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)) {
            if (text != null) {
                Spacer(modifier = Modifier.width(10.dp))
                text()
            }
        }
    }
}
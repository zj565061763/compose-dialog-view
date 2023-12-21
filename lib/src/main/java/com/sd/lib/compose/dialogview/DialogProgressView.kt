package com.sd.lib.compose.dialogview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 加载框
 */
@Composable
fun FDialogProgressView(
    modifier: Modifier = Modifier,
    progress: (@Composable () -> Unit)? = null,
    msg: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp),
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (progress != null) {
            progress()
        } else {
            CircularProgressIndicator(
                modifier = modifier.size(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                strokeWidth = 2.dp,
            )
        }

        if (msg != null) {
            Spacer(modifier = Modifier.width(5.dp))
            val textStyle = TextStyle(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                fontSize = 12.sp,
            )
            ProvideTextStyle(textStyle) {
                msg()
            }
        }
    }
}
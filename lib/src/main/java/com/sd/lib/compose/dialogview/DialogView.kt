package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.ITargetDialog
import com.sd.lib.dialog.impl.FDialog
import com.sd.lib.vtrack.tracker.ViewTracker

/**
 * 窗口
 */
@Composable
fun rememberFDialog(apply: IDialog.() -> Unit): IDialog {
    val context = LocalContext.current
    val dialog = remember(context) {
        FDialog(context as Activity).apply {
            apply(this)
        }
    }
    DisposableEffect(dialog) {
        onDispose {
            dialog.dismiss()
        }
    }
    return dialog
}

/**
 * 确认窗口
 */
@Composable
fun rememberFDialogConfirm(apply: FDialogConfirm.() -> Unit): FDialogConfirm {
    val context = LocalContext.current
    val dialog = remember(context) {
        FDialogConfirm(context as Activity).apply {
            apply(this)
        }
    }
    DisposableEffect(dialog) {
        onDispose {
            dialog.dismiss()
        }
    }
    return dialog
}

/**
 * 确认窗口
 */
@Composable
fun <T> rememberFDialogMenu(apply: FDialogMenu<T>.() -> Unit): FDialogMenu<T> {
    val context = LocalContext.current
    val dialog = remember(context) {
        FDialogMenu<T>(context as Activity).apply {
            apply(this)
        }
    }
    DisposableEffect(dialog) {
        onDispose {
            dialog.dismiss()
        }
    }
    return dialog
}

/**
 * 设置内容
 */
fun IDialog.setComposable(content: @Composable (IDialog) -> Unit) {
    var view = contentView
    if (view !is ComposeView) {
        view = ComposeView(context).also {
            setContentView(it)
        }
    }
    view.setContent {
        val hook = DialogViewHook.setComposableHook
        if (hook == null) {
            content(this@setComposable)
        } else {
            hook(content, this@setComposable)
        }
    }
}

/**
 * 设置目标的位置信息
 */
fun ITargetDialog.setLayoutCoordinates(layoutCoordinates: LayoutCoordinates) {
    setTargetLocationInfo(object : ViewTracker.LocationInfo {

        override val isReady: Boolean
            get() = layoutCoordinates.isAttached

        override val width: Int
            get() = layoutCoordinates.size.width

        override val height: Int
            get() = layoutCoordinates.size.height

        override fun getCoordinate(position: IntArray) {
            val offset = layoutCoordinates.localToWindow(Offset.Zero)
            position[0] = offset.x.toInt()
            position[1] = offset.y.toInt()
        }
    })
}

@Composable
internal fun DialogButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    textStyle: TextStyle,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.heightIn(40.dp)
            .let {
                if (onClick != null) {
                    it.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        role = Role.Button,
                        onClick = onClick
                    )
                } else {
                    it
                }
            },
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        ProvideTextStyle(textStyle.copy(color = contentColor)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = content,
            )
        }
    }
}

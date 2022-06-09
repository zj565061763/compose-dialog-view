package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.impl.FDialog

object FDialogConfirmViewDefaults {
    var colors by mutableStateOf(FDialogConfirmViewColors.light())
    var typography by mutableStateOf(FDialogConfirmViewTypography())
    var shapes by mutableStateOf(FDialogConfirmViewShapes())
}

fun fDialogConfirm(
    activity: Activity,
    content: String,
    title: String? = activity.getString(R.string.lib_compose_dialog_view_confirm_text_title),
    cancel: String? = activity.getString(R.string.lib_compose_dialog_view_confirm_text_cancel),
    confirm: String? = activity.getString(R.string.lib_compose_dialog_view_confirm_text_confirm),
    onCancel: ((IDialog) -> Unit)? = { it.dismiss() },
    onConfirm: ((IDialog) -> Unit)? = { it.dismiss() },
): IDialog {
    return FDialog(activity).apply {
        setContent { dialog ->
            FDialogConfirmView(
                title = if (title.isNullOrEmpty()) null else {
                    { Text(text = title) }
                },
                cancel = if (cancel.isNullOrEmpty()) null else {
                    { Text(text = cancel) }
                },
                confirm = if (confirm.isNullOrEmpty()) null else {
                    { Text(text = confirm) }
                },
                onCancel = {
                    onCancel?.invoke(dialog)
                },
                onConfirm = {
                    onConfirm?.invoke(dialog)
                },
            ) {
                Text(text = content)
            }
        }
    }
}

@Composable
fun FDialogConfirmView(
    title: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_title)) },
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_cancel)) },
    confirm: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_confirm)) },
    onCancel: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = FDialogConfirmViewDefaults.shapes.dialog,
        color = FDialogConfirmViewDefaults.colors.background,
    ) {
        val padding = 15.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // 标题
            if (title != null) {
                ProvideTextStyle(
                    FDialogConfirmViewDefaults.typography.title.copy(
                        color = FDialogConfirmViewDefaults.colors.title
                    )
                ) {
                    title()
                }
            }

            // 内容
            ProvideTextStyle(
                FDialogConfirmViewDefaults.typography.content.copy(
                    color = FDialogConfirmViewDefaults.colors.content
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    content()
                }
            }

            // 按钮
            FDialogConfirmButtons(
                cancel = cancel,
                confirm = confirm,
                onCancel = onCancel,
                onConfirm = onConfirm,
            )
        }
    }
}

@Composable
fun FDialogConfirmButtons(
    cancel: @Composable (() -> Unit)? = null,
    confirm: @Composable (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
) {
    if (cancel == null && confirm == null) {
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (cancel != null) {
            TextButton(
                onClick = { onCancel?.invoke() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = FDialogConfirmViewDefaults.colors.buttonCancel
                ),
            ) {
                ProvideTextStyle(FDialogConfirmViewDefaults.typography.buttonCancel) {
                    cancel()
                }
            }
        }

        if (cancel != null && confirm != null) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .width((1f / LocalDensity.current.density).dp)
                    .background(color = FDialogConfirmViewDefaults.colors.divider)
            )
        }

        if (confirm != null) {
            TextButton(
                onClick = { onConfirm?.invoke() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = FDialogConfirmViewDefaults.colors.buttonConfirm
                ),
                contentPadding = PaddingValues(0.dp),
            ) {
                ProvideTextStyle(FDialogConfirmViewDefaults.typography.buttonConfirm) {
                    confirm()
                }
            }
        }
    }
}

class FDialogConfirmViewColors(
    background: Color,
    title: Color,
    content: Color,
    buttonCancel: Color,
    buttonConfirm: Color,
    divider: Color,
) {
    var background by mutableStateOf(background)
        private set

    var title by mutableStateOf(title)
        private set

    var content by mutableStateOf(content)
        private set

    var buttonCancel by mutableStateOf(buttonCancel)
        private set

    var buttonConfirm by mutableStateOf(buttonConfirm)
        private set

    var divider by mutableStateOf(divider)
        private set

    fun copy(
        background: Color = this.background,
        title: Color = this.title,
        content: Color = this.content,
        buttonCancel: Color = this.buttonCancel,
        buttonConfirm: Color = this.buttonConfirm,
        divider: Color = this.divider,
    ): FDialogConfirmViewColors = FDialogConfirmViewColors(
        background = background,
        title = title,
        content = content,
        buttonCancel = buttonCancel,
        buttonConfirm = buttonConfirm,
        divider = divider,
    )

    companion object {
        fun light(): FDialogConfirmViewColors = FDialogConfirmViewColors(
            background = Color.White,
            title = Color.Black.copy(alpha = 0.8f),
            content = Color.Black.copy(alpha = 0.6f),
            buttonCancel = Color.Black.copy(alpha = 0.35f),
            buttonConfirm = Color.Black.copy(alpha = 0.5f),
            divider = Color.Black.copy(alpha = 0.2f),
        )

        fun dark(): FDialogConfirmViewColors = FDialogConfirmViewColors(
            background = Color.Black,
            title = Color.White.copy(alpha = 0.8f),
            content = Color.White.copy(alpha = 0.6f),
            buttonCancel = Color.White.copy(alpha = 0.35f),
            buttonConfirm = Color.White.copy(alpha = 0.5f),
            divider = Color.White.copy(alpha = 0.2f),
        )
    }
}

class FDialogConfirmViewTypography(
    title: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.25.sp
    ),
    content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    buttonCancel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
    buttonConfirm: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
) {
    var title by mutableStateOf(title)
        private set

    var content by mutableStateOf(content)
        private set

    var buttonCancel by mutableStateOf(buttonCancel)
        private set

    var buttonConfirm by mutableStateOf(buttonConfirm)
        private set

    fun copy(
        title: TextStyle = this.title,
        content: TextStyle = this.content,
        buttonCancel: TextStyle = this.buttonCancel,
        buttonConfirm: TextStyle = this.buttonConfirm,
    ): FDialogConfirmViewTypography = FDialogConfirmViewTypography(
        title = title,
        content = content,
        buttonCancel = buttonCancel,
        buttonConfirm = buttonConfirm,
    )
}

class FDialogConfirmViewShapes(
    dialog: Shape = RoundedCornerShape(8.dp),
) {
    var dialog by mutableStateOf(dialog)
        private set

    fun copy(
        dialog: Shape = this.dialog,
    ): FDialogConfirmViewShapes = FDialogConfirmViewShapes(
        dialog = dialog,
    )
}
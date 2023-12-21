package com.sd.lib.compose.dialogview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.lib.compose.dialog.R

@Composable
fun FDialogConfirmView(
    modifier: Modifier = Modifier,
    /** 形状 */
    shapes: FDialogConfirmViewShapes = FDialogConfirmViewDefaults.shapes,
    /** 颜色 */
    colors: FDialogConfirmViewColors = FDialogConfirmViewDefaults.colors,
    /** 字体 */
    typography: FDialogConfirmViewTypography = FDialogConfirmViewDefaults.typography,

    /** 标题 */
    title: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_title)) },
    /** 取消按钮 */
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_cancel)) },
    /** 确认按钮 */
    confirm: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_confirm)) },

    /** 是否显示分割线 */
    showDivider: Boolean = true,
    /** 按钮 */
    buttons: @Composable (() -> Unit)? = null,

    /** 点击取消 */
    onClickCancel: () -> Unit,
    /** 点击确认 */
    onClickConfirm: () -> Unit,

    /** 内容 */
    content: @Composable () -> Unit,
) {
    DialogConfirmView(
        modifier = modifier,
        shapes = shapes,
        colors = colors,
        typography = typography,
        params = FDialogConfirmViewParams(
            title = title,
            cancel = cancel,
            confirm = confirm,
            showDivider = showDivider,
            buttons = buttons,
            onClickCancel = onClickCancel,
            onClickConfirm = onClickConfirm,
            content = content,
        ),
    )
}

@Immutable
data class FDialogConfirmViewParams(
    /** 标题 */
    val title: @Composable (() -> Unit)?,

    /** 取消按钮 */
    val cancel: @Composable (() -> Unit)?,

    /** 确认按钮 */
    val confirm: @Composable (() -> Unit)?,

    /** 是否显示分割线 */
    val showDivider: Boolean,

    /** 按钮 */
    val buttons: @Composable (() -> Unit)?,

    /** 点击取消 */
    val onClickCancel: (() -> Unit)?,

    /** 点击确认 */
    val onClickConfirm: (() -> Unit)?,

    /** 内容 */
    val content: @Composable () -> Unit,
)

@Composable
private fun DialogConfirmView(
    modifier: Modifier = Modifier,
    shapes: FDialogConfirmViewShapes,
    colors: FDialogConfirmViewColors,
    typography: FDialogConfirmViewTypography,
    params: FDialogConfirmViewParams,
) {
    @Suppress("NAME_SHADOWING")
    val params = DialogHook.confirmViewParamsHook(params)

    val title = params.title
    val cancel = params.cancel
    val confirm = params.confirm
    val showDivider = params.showDivider
    val buttons = params.buttons
    val onClickCancel = params.onClickCancel
    val onClickConfirm = params.onClickConfirm
    val content = params.content

    Surface(
        modifier = modifier,
        shape = shapes.dialog,
        color = colors.background,
        contentColor = colors.onBackground,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 标题
                if (title != null) {
                    ProvideTextStyle(
                        typography.title.takeIf { it.color.isSpecified }
                            ?: typography.title.copy(color = colors.title)
                    ) {
                        title()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // 内容
                ProvideTextStyle(
                    typography.content.takeIf { it.color.isSpecified }
                        ?: typography.content.copy(color = colors.content)
                ) {
                    content()
                }
            }

            // 分割线
            if (cancel != null || confirm != null || buttons != null) {
                if (showDivider) {
                    LibDialogDivider(color = colors.divider)
                }
            }

            // 按钮
            if (buttons != null) {
                buttons()
            } else {
                FDialogConfirmButtons(
                    colors = colors,
                    typography = typography,
                    cancel = cancel,
                    confirm = confirm,
                    showDivider = showDivider,
                    onClickCancel = onClickCancel,
                    onClickConfirm = onClickConfirm,
                )
            }
        }
    }
}

@Composable
private fun FDialogConfirmButtons(
    colors: FDialogConfirmViewColors,
    typography: FDialogConfirmViewTypography,
    cancel: @Composable (() -> Unit)? = null,
    confirm: @Composable (() -> Unit)? = null,
    showDivider: Boolean = true,
    onClickCancel: (() -> Unit)? = null,
    onClickConfirm: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (cancel != null) {
            LibDialogButton(
                modifier = Modifier.weight(1f),
                contentColor = colors.cancel,
                textStyle = typography.cancel,
                onClick = onClickCancel,
                content = { cancel() },
            )
        }

        if (cancel != null && confirm != null) {
            if (showDivider) {
                LibDialogDivider(
                    color = colors.divider,
                    isHorizontal = false,
                )
            }
        }

        if (confirm != null) {
            LibDialogButton(
                modifier = Modifier.weight(1f),
                contentColor = colors.confirm,
                textStyle = typography.confirm,
                onClick = onClickConfirm,
                content = { confirm() },
            )
        }
    }
}

object FDialogConfirmViewDefaults {
    /** 颜色 */
    var colors by mutableStateOf(FDialogConfirmViewColors.light())

    /** 字体 */
    var typography by mutableStateOf(FDialogConfirmViewTypography())

    /** 形状 */
    var shapes by mutableStateOf(FDialogConfirmViewShapes())
}

/**
 * 颜色
 */
@Immutable
data class FDialogConfirmViewColors(
    /** 是否亮色 */
    val isLight: Boolean,

    /** 背景 */
    val background: Color,

    /** 背景上面的颜色 */
    val onBackground: Color,

    /** 标题 */
    val title: Color,

    /** 内容 */
    val content: Color,

    /** 取消按钮 */
    val cancel: Color,

    /** 确认按钮 */
    val confirm: Color,

    /** 分割线 */
    val divider: Color,
) {
    companion object {
        /**
         * 亮色
         */
        fun light(): FDialogConfirmViewColors {
            val background = Color.White
            val onBackground = Color.Black
            return FDialogConfirmViewColors(
                isLight = true,
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.9f),
                content = onBackground.copy(alpha = 0.7f),
                cancel = onBackground.copy(alpha = 0.45f),
                confirm = onBackground.copy(alpha = 0.7f),
                divider = onBackground.copy(alpha = 0.2f),
            )
        }

        /**
         * 暗色
         */
        fun dark(): FDialogConfirmViewColors {
            val background = Color.Black
            val onBackground = Color.White
            return FDialogConfirmViewColors(
                isLight = false,
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.9f),
                content = onBackground.copy(alpha = 0.7f),
                cancel = onBackground.copy(alpha = 0.45f),
                confirm = onBackground.copy(alpha = 0.7f),
                divider = onBackground.copy(alpha = 0.2f),
            )
        }
    }
}

/**
 * 字体
 */
@Immutable
data class FDialogConfirmViewTypography(
    /** 标题 */
    val title: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.25.sp
    ),

    /** 内容 */
    val content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),

    /** 取消按钮 */
    val cancel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),

    /** 确认按钮 */
    val confirm: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
)

/**
 * 形状
 */
@Immutable
data class FDialogConfirmViewShapes(
    /** 窗口形状 */
    val dialog: Shape = RoundedCornerShape(10.dp),
)
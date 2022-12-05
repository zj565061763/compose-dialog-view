package com.sd.lib.compose.dialogview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.lib.compose.dialog.R
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog

open class FDialogConfirm(activity: Activity) : FDialog(activity) {
    /** 标题 */
    var title by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 内容 */
    var content by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 取消按钮 */
    var cancel by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 确认按钮 */
    var confirm by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 点击取消 */
    var onClickCancel: ((IDialog) -> Unit)? = { it.dismiss() }
    /** 点击确认 */
    var onClickConfirm: ((IDialog) -> Unit)? = { it.dismiss() }

    override fun onCreate() {
        super.onCreate()
        setComposable {
            Content()
        }
    }

    @Composable
    protected open fun Content(
        modifier: Modifier = Modifier,
    ) {
        FDialogConfirmView(
            modifier = modifier,
            title = title,
            content = content ?: {},
            cancel = cancel,
            confirm = confirm,
            onClickCancel = {
                onClickCancel?.invoke(this@FDialogConfirm)
            },
            onClickConfirm = {
                onClickConfirm?.invoke(this@FDialogConfirm)
            },
        )
    }

    /**
     * 设置标题文字
     */
    fun setTextTitle(text: String) {
        this.title = { Text(text = text) }
    }

    /**
     * 设置内容文字
     */
    fun setTextContent(text: String) {
        this.content = { Text(text = text) }
    }

    /**
     * 设置取消按钮的文字
     */
    fun setTextCancel(text: String) {
        this.cancel = { Text(text = text) }
    }

    /**
     * 设置确认按钮的文字
     */
    fun setTextConfirm(text: String) {
        this.confirm = { Text(text = text) }
    }

    init {
        title = {
            Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_title))
        }
        cancel = {
            Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_cancel))
        }
        confirm = {
            Text(text = stringResource(id = R.string.lib_compose_dialog_view_confirm_text_confirm))
        }
    }
}

val LocalFDialogConfirmViewColors = staticCompositionLocalOf { FDialogConfirmViewDefaults.colors }

val LocalFDialogConfirmViewTypography = staticCompositionLocalOf { FDialogConfirmViewDefaults.typography }

val LocalFDialogConfirmViewShapes = staticCompositionLocalOf { FDialogConfirmViewDefaults.shapes }

@Composable
fun FDialogConfirmView(
    modifier: Modifier = Modifier,
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
    onClickCancel: (() -> Unit)? = null,
    /** 点击确认 */
    onClickConfirm: (() -> Unit)? = null,
    /** 内容 */
    content: @Composable () -> Unit,
) {
    val params = FDialogConfirmViewParams().apply {
        this.title = title
        this.cancel = cancel
        this.confirm = confirm
        this.showDivider = showDivider
        this.buttons = buttons
        this.onClickCancel = onClickCancel
        this.onClickConfirm = onClickConfirm
        this.content = content
    }
    DialogViewHook.confirmViewParamsHook?.invoke(params)

    val title = params.title
    val cancel = params.cancel
    val confirm = params.confirm
    val showDivider = params.showDivider
    val buttons = params.buttons
    val onClickCancel = params.onClickCancel
    val onClickConfirm = params.onClickConfirm
    val content = params.content

    val shapes = LocalFDialogConfirmViewShapes.current
    val colors = LocalFDialogConfirmViewColors.current
    val typography = LocalFDialogConfirmViewTypography.current

    Surface(
        shape = shapes.dialog,
        color = colors.background,
        contentColor = colors.onBackground,
        modifier = modifier,
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
                    typography.title.takeIf { it.color.isSpecified }
                        ?: typography.title.copy(color = colors.title)
                ) {
                    title()
                }
            }

            // 内容
            ProvideTextStyle(
                typography.content.takeIf { it.color.isSpecified }
                    ?: typography.content.copy(color = colors.content)
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

            // 分割线
            if (cancel != null || confirm != null || buttons != null) {
                if (showDivider) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((1f / LocalDensity.current.density).dp)
                            .background(color = colors.divider)
                    )
                }
            }

            // 按钮
            if (buttons == null) {
                FDialogConfirmButtons(
                    cancel = cancel,
                    confirm = confirm,
                    showDivider = showDivider,
                    onClickCancel = onClickCancel,
                    onClickConfirm = onClickConfirm,
                )
            } else {
                buttons()
            }
        }
    }
}

class FDialogConfirmViewParams {
    /** 标题 */
    var title: @Composable (() -> Unit)? = null
    /** 取消按钮 */
    var cancel: @Composable (() -> Unit)? = null
    /** 确认按钮 */
    var confirm: @Composable (() -> Unit)? = null
    /** 是否显示分割线 */
    var showDivider: Boolean = true
    /** 按钮 */
    var buttons: @Composable (() -> Unit)? = null
    /** 点击取消 */
    var onClickCancel: (() -> Unit)? = null
    /** 点击确认 */
    var onClickConfirm: (() -> Unit)? = null
    /** 内容 */
    var content: @Composable () -> Unit = {}
}

@Composable
private fun FDialogConfirmButtons(
    cancel: @Composable (() -> Unit)? = null,
    confirm: @Composable (() -> Unit)? = null,
    showDivider: Boolean = true,
    onClickCancel: (() -> Unit)? = null,
    onClickConfirm: (() -> Unit)? = null,
) {
    val colors = LocalFDialogConfirmViewColors.current
    val typography = LocalFDialogConfirmViewTypography.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (cancel != null) {
            FDialogButton(
                modifier = Modifier.weight(1f),
                backgroundColor = Color.Transparent,
                contentColor = colors.buttonCancel,
                textStyle = typography.buttonCancel,
                onClick = onClickCancel,
                content = { cancel() },
            )
        }

        if (cancel != null && confirm != null) {
            if (showDivider) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width((1f / LocalDensity.current.density).dp)
                        .background(color = colors.divider)
                )
            }
        }

        if (confirm != null) {
            FDialogButton(
                modifier = Modifier.weight(1f),
                backgroundColor = Color.Transparent,
                contentColor = colors.buttonConfirm,
                textStyle = typography.buttonConfirm,
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
data class FDialogConfirmViewColors(
    /** 背景 */
    val background: Color,
    /** 在背景上面内容颜色 */
    val onBackground: Color,
    /** 标题 */
    val title: Color,
    /** 内容 */
    val content: Color,
    /** 取消按钮 */
    val buttonCancel: Color,
    /** 确认按钮 */
    val buttonConfirm: Color,
    /** 分割线 */
    val divider: Color,
    /** 是否亮色 */
    val isLight: Boolean,
) {
    companion object {
        fun light(): FDialogConfirmViewColors {
            val background = Color.White
            val onBackground = Color.Black
            return FDialogConfirmViewColors(
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.9f),
                content = onBackground.copy(alpha = 0.7f),
                buttonCancel = onBackground.copy(alpha = 0.45f),
                buttonConfirm = onBackground.copy(alpha = 0.7f),
                divider = onBackground.copy(alpha = 0.2f),
                isLight = true,
            )
        }

        fun dark(): FDialogConfirmViewColors {
            val background = Color.Black
            val onBackground = Color.White
            return FDialogConfirmViewColors(
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.9f),
                content = onBackground.copy(alpha = 0.7f),
                buttonCancel = onBackground.copy(alpha = 0.45f),
                buttonConfirm = onBackground.copy(alpha = 0.7f),
                divider = onBackground.copy(alpha = 0.2f),
                isLight = false,
            )
        }
    }
}

/**
 * 字体
 */
data class FDialogConfirmViewTypography(
    /** 标题 */
    val title: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.25.sp
    ),
    /** 内容 */
    val content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    /** 取消按钮 */
    val buttonCancel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
    /** 确认按钮 */
    val buttonConfirm: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
)

/**
 * 形状
 */
data class FDialogConfirmViewShapes(
    /** 窗口形状 */
    val dialog: Shape = RoundedCornerShape(8.dp),
)
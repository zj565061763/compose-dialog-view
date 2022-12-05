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
import com.sd.lib.compose.dialog.R
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.impl.FDialog

class FDialogConfirm(activity: Activity) : FDialog(activity) {
    /** 内容 */
    var content by mutableStateOf("")
    /** 自定义内容，优先级高于[content] */
    var composableContent by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 标题 */
    var title by mutableStateOf<String?>(

        activity.getString(R.string.lib_compose_dialog_view_confirm_text_title)
    )
    /** 取消按钮 */
    var cancel by mutableStateOf<String?>(
        activity.getString(R.string.lib_compose_dialog_view_confirm_text_cancel)
    )
    /** 确认按钮 */
    var confirm by mutableStateOf<String?>(
        activity.getString(R.string.lib_compose_dialog_view_confirm_text_confirm)
    )

    /** 点击取消 */
    var onClickCancel: ((IDialog) -> Unit)? = { it.dismiss() }
    /** 点击确认 */
    var onClickConfirm: ((IDialog) -> Unit)? = { it.dismiss() }

    override fun onCreate() {
        super.onCreate()
        setComposable {
            val title = title
            val cancel = cancel
            val confirm = confirm
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
                onClickCancel = {
                    onClickCancel?.invoke(this@FDialogConfirm)
                },
                onClickConfirm = {
                    onClickConfirm?.invoke(this@FDialogConfirm)
                },
                content = composableContent ?: { Text(text = content) }
            )
        }
    }
}

@Composable
fun FDialogConfirmView(
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

    Surface(
        shape = FDialogConfirmViewDefaults.shapes.dialog,
        color = FDialogConfirmViewDefaults.colors.background,
        contentColor = FDialogConfirmViewDefaults.colors.onBackground,
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

            if (cancel != null || confirm != null) {
                if (showDivider) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((1f / LocalDensity.current.density).dp)
                            .background(color = FDialogConfirmViewDefaults.colors.divider)
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

@Composable
private fun FDialogConfirmButtons(
    cancel: @Composable (() -> Unit)? = null,
    confirm: @Composable (() -> Unit)? = null,
    showDivider: Boolean = true,
    onClickCancel: (() -> Unit)? = null,
    onClickConfirm: (() -> Unit)? = null,
) {
    if (cancel == null && confirm == null) {
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (cancel != null) {
            DialogButton(
                modifier = Modifier.weight(1f),
                backgroundColor = FDialogConfirmViewDefaults.colors.background,
                contentColor = FDialogConfirmViewDefaults.colors.buttonCancel,
                textStyle = FDialogConfirmViewDefaults.typography.buttonCancel,
                onClick = { onClickCancel?.invoke() },
                content = { cancel() }
            )
        }

        if (cancel != null && confirm != null) {
            if (showDivider) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width((1f / LocalDensity.current.density).dp)
                        .background(color = FDialogConfirmViewDefaults.colors.divider)
                )
            }
        }

        if (confirm != null) {
            DialogButton(
                modifier = Modifier.weight(1f),
                backgroundColor = FDialogConfirmViewDefaults.colors.background,
                contentColor = FDialogConfirmViewDefaults.colors.buttonConfirm,
                textStyle = FDialogConfirmViewDefaults.typography.buttonConfirm,
                onClick = { onClickConfirm?.invoke() },
                content = { confirm() }
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
    lateinit var content: @Composable () -> Unit
}

class FDialogConfirmViewColors(
    /** 背景 */
    background: Color,
    /** 在背景上面内容颜色 */
    onBackground: Color,
    /** 标题 */
    title: Color,
    /** 内容 */
    content: Color,
    /** 取消按钮 */
    buttonCancel: Color,
    /** 确认按钮 */
    buttonConfirm: Color,
    /** 分割线 */
    divider: Color,
    /** 是否亮色 */
    isLight: Boolean,
) {
    var background by mutableStateOf(background)
        private set

    var onBackground by mutableStateOf(onBackground)
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

    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        title: Color = this.title,
        content: Color = this.content,
        buttonCancel: Color = this.buttonCancel,
        buttonConfirm: Color = this.buttonConfirm,
        divider: Color = this.divider,
        isLight: Boolean = this.isLight,
    ): FDialogConfirmViewColors = FDialogConfirmViewColors(
        background = background,
        onBackground = onBackground,
        title = title,
        content = content,
        buttonCancel = buttonCancel,
        buttonConfirm = buttonConfirm,
        divider = divider,
        isLight = isLight,
    )

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
                buttonConfirm = onBackground.copy(alpha = 0.6f),
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
                buttonConfirm = onBackground.copy(alpha = 0.6f),
                divider = onBackground.copy(alpha = 0.2f),
                isLight = false,
            )
        }
    }
}

class FDialogConfirmViewTypography(
    /** 标题 */
    title: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.25.sp
    ),
    /** 内容 */
    content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    /** 取消按钮 */
    buttonCancel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
    /** 确认按钮 */
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
    /** 窗口形状 */
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
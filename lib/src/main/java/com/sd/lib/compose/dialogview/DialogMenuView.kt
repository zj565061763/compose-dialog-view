package com.sd.lib.compose.dialogview

import android.app.Activity
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.animator.SlideTopBottomCreator
import com.sd.lib.dialog.impl.FDialog

class FDialogMenu<T>(activity: Activity) : FDialog(activity) {
    /** 数据 */
    var data = mutableStateListOf<T>()
    /** 自定义每一行的样式 */
    var row: @Composable (RowScope.(index: Int, item: T) -> Unit)? = null

    /** 标题 */
    var title by mutableStateOf<String?>(null)
    /** 取消按钮 */
    var cancel by mutableStateOf<String?>(activity.getString(R.string.lib_compose_dialog_view_menu_text_cancel))

    /** 点击取消 */
    var onClickCancel: ((IDialog) -> Unit)? = { it.dismiss() }
    /** 点击某一行 */
    lateinit var onClickRow: (index: Int, item: T, dialog: IDialog) -> Unit

    override fun onCreate() {
        super.onCreate()
        setComposable {
            val title = title
            val cancel = cancel
            FDialogMenuView(
                title = if (title.isNullOrEmpty()) null else {
                    { Text(text = title) }
                },
                cancel = if (cancel.isNullOrEmpty()) null else {
                    { Text(text = cancel) }
                },
                onClickCancel = {
                    onClickCancel?.invoke(this@FDialogMenu)
                },
                data = data,
                row = row,
                onClickRow = { index, item ->
                    onClickRow.invoke(index, item, this@FDialogMenu)
                },
            )
        }
    }

    init {
        setPadding(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorCreator = SlideTopBottomCreator()
    }
}

@Composable
fun <T> FDialogMenuView(
    /** 数据 */
    data: List<T>,
    /** 每一行要显示的界面 */
    row: @Composable (RowScope.(index: Int, item: T) -> Unit)? = null,
    /** 标题 */
    title: @Composable (() -> Unit)? = null,
    /** 取消按钮 */
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) },
    /** 点击取消 */
    onClickCancel: (() -> Unit)? = null,
    /** 点击某一行 */
    onClickRow: (index: Int, item: T) -> Unit,
) {
    Surface(
        shape = FDialogMenuViewDefaults.shapes.dialog,
        color = FDialogMenuViewDefaults.colors.background,
        contentColor = FDialogMenuViewDefaults.colors.onBackground,
    ) {
        Column(
            modifier = Modifier
                .background(FDialogMenuViewDefaults.colors.divider)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // 标题
            if (title != null) {
                DialogButton(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = FDialogMenuViewDefaults.colors.background,
                    contentColor = FDialogMenuViewDefaults.colors.title,
                    textStyle = FDialogMenuViewDefaults.typography.title,
                    content = { title() }
                )
                Spacer(modifier = Modifier.height((1f / LocalDensity.current.density).dp))
            }

            // 内容
            val maxHeight = LocalContext.current.resources.displayMetrics.heightPixels / 2f
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = with(LocalDensity.current) { maxHeight.toDp() }),
                verticalArrangement = Arrangement.spacedBy((1f / LocalDensity.current.density).dp),
            ) {
                items(count = data.size) { index ->
                    DialogButton(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = FDialogMenuViewDefaults.colors.background,
                        contentColor = FDialogMenuViewDefaults.colors.content,
                        textStyle = FDialogMenuViewDefaults.typography.content,
                        onClick = { onClickRow.invoke(index, data[index]) },
                        content = {
                            if (row != null) {
                                row(index, data[index])
                            } else {
                                Text(data[index].toString())
                            }
                        }
                    )
                }
            }

            // 取消按钮
            if (cancel != null) {
                Spacer(modifier = Modifier.height(10.dp))
                DialogButton(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = FDialogMenuViewDefaults.colors.background,
                    contentColor = FDialogMenuViewDefaults.colors.buttonCancel,
                    textStyle = FDialogMenuViewDefaults.typography.buttonCancel,
                    onClick = { onClickCancel?.invoke() },
                    content = { cancel() }
                )
            }
        }
    }
}

object FDialogMenuViewDefaults {
    /** 颜色 */
    var colors by mutableStateOf(FDialogMenuViewColors.light())
    /** 字体 */
    var typography by mutableStateOf(FDialogMenuViewTypography())
    /** 形状 */
    var shapes by mutableStateOf(FDialogMenuViewShapes())
}

class FDialogMenuViewColors(
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
        divider: Color = this.divider,
        isLight: Boolean = this.isLight,
    ): FDialogMenuViewColors = FDialogMenuViewColors(
        background = background,
        onBackground = onBackground,
        title = title,
        content = content,
        buttonCancel = buttonCancel,
        divider = divider,
        isLight = isLight,
    )

    companion object {
        /**
         * 亮色
         */
        fun light(): FDialogMenuViewColors {
            val background = Color.White
            val onBackground = Color.Black
            return FDialogMenuViewColors(
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.9f),
                content = onBackground.copy(alpha = 0.65f),
                buttonCancel = onBackground.copy(alpha = 0.4f),
                divider = onBackground.copy(alpha = 0.1f),
                isLight = true,
            )
        }

        /**
         * 暗色
         */
        fun dark(): FDialogMenuViewColors {
            val background = Color.Black
            val onBackground = Color.White
            return FDialogMenuViewColors(
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.9f),
                content = onBackground.copy(alpha = 0.65f),
                buttonCancel = onBackground.copy(alpha = 0.4f),
                divider = onBackground.copy(alpha = 0.1f),
                isLight = false,
            )
        }
    }
}

class FDialogMenuViewTypography(
    /** 标题 */
    title: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.25.sp,
    ),
    /** 内容 */
    content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        textAlign = TextAlign.Center,
    ),
    /** 取消按钮 */
    buttonCancel: TextStyle = TextStyle(
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

    fun copy(
        title: TextStyle = this.title,
        content: TextStyle = this.content,
        buttonCancel: TextStyle = this.buttonCancel,
    ): FDialogMenuViewTypography = FDialogMenuViewTypography(
        title = title,
        content = content,
        buttonCancel = buttonCancel,
    )
}

class FDialogMenuViewShapes(
    /** 窗口形状 */
    dialog: Shape = RoundedCornerShape(0.dp),
) {
    var dialog by mutableStateOf(dialog)
        private set

    fun copy(
        dialog: Shape = this.dialog,
    ): FDialogMenuViewShapes = FDialogMenuViewShapes(
        dialog = dialog,
    )
}
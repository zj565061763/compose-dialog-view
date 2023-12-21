package com.sd.lib.compose.dialogview

import android.content.Context
import android.view.Gravity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.sd.lib.compose.dialog.R
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog
import com.sd.lib.vdialog.animator.slide.SlideUpDownRItselfFactory

class FDialogMenu<T>(context: Context) : FDialog(context) {
    /** 数据 */
    val data = mutableStateListOf<T>()

    /** 标题 */
    private var _title by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 每一行的布局 */
    private var _row by mutableStateOf<@Composable (RowScope.(index: Int, item: T) -> Unit)?>(null)

    /** 取消按钮 */
    private var _cancel by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 点击某一行 */
    private var _onClickRow: ((index: Int, item: T, dialog: IDialog) -> Unit)? = null

    /** 点击取消 */
    private var _onClickCancel: ((IDialog) -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        setComposable {
            FDialogMenuView(
                data = data,
                title = _title,
                row = _row,
                cancel = _cancel,
                onClickCancel = {
                    _onClickCancel?.invoke(this@FDialogMenu)
                },
                onClickRow = { index, item ->
                    _onClickRow?.invoke(index, item, this@FDialogMenu)
                },
            )
        }
    }

    /**
     * 设置标题
     */
    fun setTitle(block: @Composable (() -> Unit)?) {
        this._title = block
    }

    /**
     * 设置行的布局
     */
    fun setRow(block: @Composable (RowScope.(index: Int, item: T) -> Unit)?) {
        this._row = block
    }

    /**
     * 设置取消按钮
     */
    fun setCancel(block: @Composable (() -> Unit)?) {
        this._cancel = block
    }

    /**
     * 点击行
     */
    fun onClickRow(callback: ((index: Int, item: T, dialog: IDialog) -> Unit)?) {
        _onClickRow = callback
    }

    /**
     * 点击取消
     */
    fun onClickCancel(callback: ((IDialog) -> Unit)?) {
        _onClickCancel = callback ?: { dismiss() }
    }

    init {
        padding.set(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorFactory = SlideUpDownRItselfFactory()
        setCancel { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) }
        onClickCancel { dismiss() }
    }
}


@Composable
fun <T> FDialogMenuView(
    /** 数据 */
    data: List<T>,

    modifier: Modifier = Modifier,
    /** 形状 */
    shapes: FDialogMenuViewShapes = FDialogMenuViewDefaults.shapes,
    /** 颜色 */
    colors: FDialogMenuViewColors = FDialogMenuViewDefaults.colors,
    /** 字体 */
    typography: FDialogMenuViewTypography = FDialogMenuViewDefaults.typography,

    /** 标题 */
    title: @Composable (() -> Unit)? = null,
    /** 每一行要显示的界面 */
    row: @Composable (RowScope.(index: Int, item: T) -> Unit)? = null,
    /** 取消按钮 */
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) },

    /** 点击取消 */
    onClickCancel: (() -> Unit)? = null,
    /** 点击某一行 */
    onClickRow: (index: Int, item: T) -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shapes.dialog,
        color = colors.background,
        contentColor = colors.onBackground,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 标题
            if (title != null) {
                LibDialogButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(50.dp),
                    contentColor = colors.title,
                    textStyle = typography.title,
                    content = { title() }
                )
                LibDialogDivider(color = colors.divider)
            }

            // 内容
            val maxHeight = LocalContext.current.resources.displayMetrics.heightPixels / 2f
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = with(LocalDensity.current) { maxHeight.toDp() }),
            ) {
                items(count = data.size) { index ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        LibDialogButton(
                            modifier = Modifier.fillMaxWidth(),
                            contentColor = colors.content,
                            textStyle = typography.content,
                            onClick = { onClickRow.invoke(index, data[index]) },
                            content = {
                                if (row != null) {
                                    row(index, data[index])
                                } else {
                                    Text(data[index].toString())
                                }
                            }
                        )
                        if (index != data.lastIndex) {
                            LibDialogDivider(color = colors.divider)
                        }
                    }
                }
            }

            // 取消按钮
            if (cancel != null) {
                LibDialogDivider(
                    color = colors.divider,
                    thickness = 10.dp,
                )
                LibDialogButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(50.dp),
                    contentColor = colors.cancel,
                    textStyle = typography.cancel,
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

@Immutable
data class FDialogMenuViewColors(
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

    /** 分割线 */
    val divider: Color,
) {
    companion object {
        /**
         * 亮色
         */
        fun light(): FDialogMenuViewColors {
            val background = Color.White
            val onBackground = Color.Black
            return FDialogMenuViewColors(
                isLight = true,
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.3f),
                content = onBackground.copy(alpha = 0.7f),
                cancel = onBackground.copy(alpha = 0.6f),
                divider = onBackground.copy(alpha = 0.1f),
            )
        }

        /**
         * 暗色
         */
        fun dark(): FDialogMenuViewColors {
            val background = Color.Black
            val onBackground = Color.White
            return FDialogMenuViewColors(
                isLight = false,
                background = background,
                onBackground = onBackground,
                title = onBackground.copy(alpha = 0.3f),
                content = onBackground.copy(alpha = 0.7f),
                cancel = onBackground.copy(alpha = 0.6f),
                divider = onBackground.copy(alpha = 0.1f),
            )
        }
    }
}

@Immutable
data class FDialogMenuViewTypography(
    /** 标题 */
    val title: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.25.sp,
    ),

    /** 内容 */
    val content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        textAlign = TextAlign.Center,
    ),

    /** 取消按钮 */
    val cancel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
)

@Immutable
data class FDialogMenuViewShapes(
    /** 窗口形状 */
    val dialog: Shape = RoundedCornerShape(0.dp),
)
package com.sd.lib.compose.dialogview

import android.content.Context
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.sd.lib.compose.dialog.R
import com.sd.lib.vdialog.FDialog
import com.sd.lib.vdialog.IDialog
import com.sd.lib.vdialog.animator.slide.SlideUpDownRItselfFactory

open class FDialogMenu<T>(context: Context) : FDialog(context) {
    /** 数据 */
    val data = mutableStateListOf<T>()

    /** 每一行的布局 */
    internal var row by mutableStateOf<@Composable (RowScope.(index: Int, item: T) -> Unit)?>(null)

    /** 标题 */
    internal var title by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 取消按钮 */
    internal var cancel by mutableStateOf<@Composable (() -> Unit)?>(null)

    /** 点击某一行 */
    private var _onClickRow: ((index: Int, item: T, dialog: IDialog) -> Unit)? = null
    /** 点击取消 */
    private var _onClickCancel: ((IDialog) -> Unit)? = { it.dismiss() }

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
        FDialogMenuView(
            modifier = modifier,
            data = data,
            row = row,
            title = title,
            cancel = cancel,
            onClickCancel = {
                _onClickCancel?.invoke(this@FDialogMenu)
            },
            onClickRow = { index, item ->
                _onClickRow?.invoke(index, item, this@FDialogMenu)
            },
        )
    }

    /**
     * 点击取消
     */
    fun onClickCancel(callback: ((IDialog) -> Unit)?) {
        _onClickCancel = callback
    }

    /**
     * 点击某一行
     */
    fun onClickRow(callback: ((index: Int, item: T, dialog: IDialog) -> Unit)?) {
        _onClickRow = callback
    }

    /**
     * 设置标题文字
     */
    fun setTextTitle(text: String?) {
        this.title = if (text == null) {
            null
        } else {
            { Text(text = text) }
        }
    }

    /**
     * 设置取消按钮的文字
     */
    fun setTextCancel(text: String?) {
        this.cancel = if (text == null) {
            null
        } else {
            { Text(text = text) }
        }
    }

    init {
        padding.set(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorFactory = SlideUpDownRItselfFactory()

        cancel = {
            Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel))
        }
    }
}

/**
 * 设置每一行的布局
 */
fun <T> FDialogMenu<T>.setRow(block: @Composable (RowScope.(index: Int, item: T) -> Unit)?) {
    this.row = block
}

/**
 * 设置标题
 */
fun <T> FDialogMenu<T>.setTitle(block: @Composable (() -> Unit)?) {
    this.title = block
}

/**
 * 设置取消按钮
 */
fun <T> FDialogMenu<T>.setCancel(block: @Composable (() -> Unit)?) {
    this.cancel = block
}

private val LocalFDialogMenuViewColors = staticCompositionLocalOf<FDialogMenuViewColors?> { null }

private val LocalFDialogMenuViewTypography = staticCompositionLocalOf<FDialogMenuViewTypography?> { null }

private val LocalFDialogMenuViewShapes = staticCompositionLocalOf<FDialogMenuViewShapes?> { null }

@Composable
fun <T> FDialogMenuView(
    modifier: Modifier = Modifier,
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
    val shapes = LocalFDialogMenuViewShapes.current ?: FDialogMenuViewDefaults.shapes
    val colors = LocalFDialogMenuViewColors.current ?: FDialogMenuViewDefaults.colors
    val typography = LocalFDialogMenuViewTypography.current ?: FDialogMenuViewDefaults.typography

    Surface(
        shape = shapes.dialog,
        color = colors.background,
        contentColor = colors.onBackground,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // 标题
            if (title != null) {
                FDialogButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(50.dp),
                    backgroundColor = Color.Transparent,
                    contentColor = colors.title,
                    textStyle = typography.title,
                    content = { title() }
                )
                FDialogDivider(color = colors.divider)
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
                        FDialogButton(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.Transparent,
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
                            FDialogDivider(color = colors.divider)
                        }
                    }
                }
            }

            // 取消按钮
            if (cancel != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = colors.divider)
                )
                FDialogButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(50.dp),
                    backgroundColor = Color.Transparent,
                    contentColor = colors.buttonCancel,
                    textStyle = typography.buttonCancel,
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
    val background: Color,
    /** 在背景上面内容颜色 */
    val onBackground: Color,
    /** 标题 */
    val title: Color,
    /** 内容 */
    val content: Color,
    /** 取消按钮 */
    val buttonCancel: Color,
    /** 分割线 */
    val divider: Color,
    /** 是否亮色 */
    val isLight: Boolean,
) {
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
                title = onBackground.copy(alpha = 0.3f),
                content = onBackground.copy(alpha = 0.7f),
                buttonCancel = onBackground.copy(alpha = 0.6f),
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
                title = onBackground.copy(alpha = 0.3f),
                content = onBackground.copy(alpha = 0.7f),
                buttonCancel = onBackground.copy(alpha = 0.6f),
                divider = onBackground.copy(alpha = 0.1f),
                isLight = false,
            )
        }
    }
}

class FDialogMenuViewTypography(
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
    val buttonCancel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
)

class FDialogMenuViewShapes(
    /** 窗口形状 */
    val dialog: Shape = RoundedCornerShape(0.dp),
)
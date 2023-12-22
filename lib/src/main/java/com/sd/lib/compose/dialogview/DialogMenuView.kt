package com.sd.lib.compose.dialogview

import android.content.Context
import android.view.Gravity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
    var data by mutableStateOf(listOf<T>())

    /** 标题 */
    var title by mutableStateOf<@Composable (() -> Unit)?>(null)
    /** 取消按钮 */
    var cancel by mutableStateOf<@Composable (() -> Unit)?>(null)
    /** 行的布局 */
    var row by mutableStateOf<@Composable (RowScope.(index: Int, item: T) -> Unit)?>(null)

    /** 行的文字 */
    var rowText by mutableStateOf<((index: Int, item: T) -> String)?>(null)
    /** 行的key */
    var rowKey by mutableStateOf<((index: Int, item: T) -> Any)?>(null)
    /** 行的内容类型 */
    var rowContentType by mutableStateOf<((index: Int, item: T) -> Any?)?>(null)

    /** 列表项 */
    var content by mutableStateOf<(LazyListScope.() -> Unit)?>(null)

    /** 点击取消 */
    var onClickCancel: ((IDialog) -> Unit)? = null
    /** 点击某一行 */
    var onClickRow: ((index: Int, item: T, dialog: IDialog) -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        setComposable {
            val cnt = content
            if (cnt == null) {
                FDialogMenuView(
                    data = data,

                    title = title,
                    cancel = cancel,
                    row = row,

                    rowText = rowText,
                    rowKey = rowKey,
                    rowContentType = rowContentType,

                    onClickCancel = {
                        onClickCancel?.invoke(this@FDialogMenu)
                    },
                    onClickRow = { index, item ->
                        onClickRow?.invoke(index, item, this@FDialogMenu)
                    },
                )
            } else {
                FDialogMenuView(
                    title = title,
                    cancel = cancel,
                    onClickCancel = {
                        onClickCancel?.invoke(this@FDialogMenu)
                    },
                    content = cnt,
                )
            }
        }
    }

    init {
        padding.set(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorFactory = SlideUpDownRItselfFactory()
        setCanceledOnTouchOutside(true)
        this.cancel = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) }
        this.onClickCancel = { dismiss() }
        DialogBehavior.menu?.invoke(this)
    }
}

/**
 * 菜单框
 */
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
    /** 取消按钮 */
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) },
    /** 行的布局 */
    row: @Composable (RowScope.(index: Int, item: T) -> Unit)? = null,

    /** 行的文字 */
    rowText: ((index: Int, item: T) -> String)? = null,
    /** 行的key */
    rowKey: ((index: Int, item: T) -> Any)? = null,
    /** 行的内容类型 */
    rowContentType: ((index: Int, item: T) -> Any?)? = null,

    /** 点击取消 */
    onClickCancel: () -> Unit,
    /** 点击某一行 */
    onClickRow: (index: Int, item: T) -> Unit,
) {
    FDialogMenuView(
        modifier = modifier,
        shapes = shapes,
        colors = colors,
        typography = typography,
        title = title,
        cancel = cancel,
        onClickCancel = onClickCancel,
    ) {
        itemsIndexed(
            items = data,
            key = rowKey,
            contentType = rowContentType ?: { _, _ -> null },
        ) { index, item ->
            Column {
                LibDialogButton(
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = colors.content,
                    textStyle = typography.content,
                    onClick = { onClickRow(index, item) },
                    content = {
                        if (row != null) {
                            row(index, item)
                        } else {
                            Text(text = rowText?.invoke(index, item) ?: item.toString())
                        }
                    }
                )
                if (index != data.lastIndex) {
                    LibDialogDivider(color = colors.divider)
                }
            }
        }
    }
}

@Composable
fun FDialogMenuView(
    modifier: Modifier = Modifier,
    /** 形状 */
    shapes: FDialogMenuViewShapes = FDialogMenuViewDefaults.shapes,
    /** 颜色 */
    colors: FDialogMenuViewColors = FDialogMenuViewDefaults.colors,
    /** 字体 */
    typography: FDialogMenuViewTypography = FDialogMenuViewDefaults.typography,

    /** 标题 */
    title: @Composable (() -> Unit)? = null,
    /** 取消按钮 */
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) },

    /** 点击取消 */
    onClickCancel: (() -> Unit)? = null,

    /** 列表项 */
    content: LazyListScope.() -> Unit,
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
                    onClick = null,
                    content = { title() },
                )
                LibDialogDivider(color = colors.divider)
            }

            // 内容
            val maxHeight = LocalContext.current.resources.displayMetrics.heightPixels / 2f
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = with(LocalDensity.current) { maxHeight.toDp() }),
                content = content,
            )

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
                    content = { cancel() },
                )
            }
        }
    }
}

object FDialogMenuViewDefaults {
    /** 形状 */
    var shapes by mutableStateOf(FDialogMenuViewShapes())

    /** 颜色 */
    var colors by mutableStateOf(FDialogMenuViewColors.light())

    /** 字体 */
    var typography by mutableStateOf(FDialogMenuViewTypography())
}

@Immutable
data class FDialogMenuViewShapes(
    /** 窗口形状 */
    val dialog: Shape = RoundedCornerShape(0.dp),
)

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
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),

    /** 内容 */
    val content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
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
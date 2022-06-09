package com.sd.lib.compose.dialogview

import android.app.Activity
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.sd.lib.dialog.IDialog
import com.sd.lib.dialog.animator.SlideTopBottomCreator
import com.sd.lib.dialog.impl.FDialog

fun <T> fDialogMenu(
    activity: Activity,
    data: List<T>,
    title: String? = null,
    cancel: String? = activity.getString(R.string.lib_compose_dialog_view_menu_text_cancel),
    onClickCancel: (IDialog) -> Unit = { it.dismiss() },
    onClick: (index: Int, item: T, dialog: IDialog) -> Unit,
): IDialog {
    return FDialog(activity).apply {
        setPadding(0, 0, 0, 0)
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        animatorCreator = SlideTopBottomCreator()

        setContent { dialog ->
            FDialogMenuView(
                title = if (title.isNullOrEmpty()) null else {
                    { Text(text = title) }
                },
                cancel = if (cancel.isNullOrEmpty()) null else {
                    { Text(text = cancel) }
                },
                onClickCancel = {
                    onClickCancel.invoke(dialog)
                },
                data = data,
                onClick = { index, item ->
                    onClick(index, item, dialog)
                },
            )
        }
    }
}

@Composable
fun <T> FDialogMenuView(
    data: List<T>,
    content: @Composable RowScope.(index: Int, item: T) -> Unit = { index, item ->
        Text(item.toString())
    },
    title: @Composable (() -> Unit)? = null,
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) },
    onClickCancel: (() -> Unit)? = null,
    onClick: (index: Int, item: T) -> Unit,
) {
    Surface(
        shape = FDialogMenuViewDefaults.shapes.dialog,
    ) {
        Column(
            modifier = Modifier
                .background(FDialogMenuViewDefaults.colors.divider)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // 标题
            if (title != null) {
                Row(
                    modifier = Modifier
                        .background(FDialogMenuViewDefaults.colors.background)
                        .fillMaxWidth()
                        .heightIn(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ProvideTextStyle(
                        FDialogMenuViewDefaults.typography.title.copy(
                            color = FDialogMenuViewDefaults.colors.title
                        )
                    ) {
                        title()
                    }
                }
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
                    TextButton(
                        onClick = { onClick(index, data[index]) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(0.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = FDialogMenuViewDefaults.colors.background,
                            contentColor = FDialogMenuViewDefaults.colors.content
                        ),
                    ) {
                        ProvideTextStyle(FDialogMenuViewDefaults.typography.content) {
                            content(index, data[index])
                        }
                    }
                }
            }

            // 按钮
            if (cancel != null) {
                Spacer(modifier = Modifier
                    .height(10.dp))

                TextButton(
                    onClick = { onClickCancel?.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(0.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = FDialogMenuViewDefaults.colors.background,
                        contentColor = FDialogMenuViewDefaults.colors.buttonCancel
                    ),
                ) {
                    ProvideTextStyle(FDialogMenuViewDefaults.typography.buttonCancel) {
                        cancel()
                    }
                }
            }
        }
    }
}

object FDialogMenuViewDefaults {
    var colors by mutableStateOf(FDialogMenuViewColors.light())
    var typography by mutableStateOf(FDialogMenuViewTypography())
    var shapes by mutableStateOf(FDialogMenuViewShapes())
}

class FDialogMenuViewColors(
    background: Color,
    title: Color,
    content: Color,
    buttonCancel: Color,
    divider: Color,
    isLight: Boolean,
) {
    var background by mutableStateOf(background)
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
        title: Color = this.title,
        content: Color = this.content,
        buttonCancel: Color = this.buttonCancel,
        divider: Color = this.divider,
        isLight: Boolean = this.isLight,
    ): FDialogMenuViewColors = FDialogMenuViewColors(
        background = background,
        title = title,
        content = content,
        buttonCancel = buttonCancel,
        divider = divider,
        isLight = isLight,
    )

    companion object {
        fun light(): FDialogMenuViewColors = FDialogMenuViewColors(
            background = Color.White,
            title = Color.Black.copy(alpha = 0.8f),
            content = Color.Black.copy(alpha = 0.6f),
            buttonCancel = Color.Black.copy(alpha = 0.35f),
            divider = Color.Black.copy(alpha = 0.1f),
            isLight = true,
        )

        fun dark(): FDialogMenuViewColors = FDialogMenuViewColors(
            background = Color.Black,
            title = Color.White.copy(alpha = 0.8f),
            content = Color.White.copy(alpha = 0.6f),
            buttonCancel = Color.White.copy(alpha = 0.35f),
            divider = Color.White.copy(alpha = 0.1f),
            isLight = false,
        )
    }
}

class FDialogMenuViewTypography(
    title: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.25.sp,
    ),
    content: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        textAlign = TextAlign.Center,
    ),
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
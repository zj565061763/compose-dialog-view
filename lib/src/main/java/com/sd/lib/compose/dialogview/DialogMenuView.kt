package com.sd.lib.compose.dialogview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FDialogMenuView(
    title: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_title)) },
    cancel: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.lib_compose_dialog_view_menu_text_cancel)) },
    onCancel: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    Surface(
        shape = FDialogMenuViewDefaults.shapes.dialog,
        color = FDialogMenuViewDefaults.colors.background,
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
                    FDialogMenuViewDefaults.typography.title.copy(
                        color = FDialogMenuViewDefaults.colors.title
                    )
                ) {
                    title()
                }
            }

            // 内容
            ProvideTextStyle(
                FDialogMenuViewDefaults.typography.content.copy(
                    color = FDialogMenuViewDefaults.colors.content
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                    content = content,
                )
            }

            // 按钮
            if (cancel != null) {
                Spacer(modifier = Modifier
                    .height(10.dp)
                    .background(FDialogMenuViewDefaults.colors.divider))

                TextButton(
                    onClick = { onCancel?.invoke() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.textButtonColors(
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
            divider = Color.Black.copy(alpha = 0.2f),
            isLight = true,
        )

        fun dark(): FDialogMenuViewColors = FDialogMenuViewColors(
            background = Color.Black,
            title = Color.White.copy(alpha = 0.8f),
            content = Color.White.copy(alpha = 0.6f),
            buttonCancel = Color.White.copy(alpha = 0.35f),
            divider = Color.White.copy(alpha = 0.2f),
            isLight = false,
        )
    }
}

class FDialogMenuViewTypography(
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
    dialog: Shape = RoundedCornerShape(4.dp),
) {
    var dialog by mutableStateOf(dialog)
        private set

    fun copy(
        dialog: Shape = this.dialog,
    ): FDialogMenuViewShapes = FDialogMenuViewShapes(
        dialog = dialog,
    )
}
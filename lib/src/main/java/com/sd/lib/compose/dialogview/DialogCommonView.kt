package com.sd.lib.compose.dialogview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun FDialogButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    textStyle: TextStyle,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.heightIn(40.dp)
            .let {
                if (onClick != null) {
                    it.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        role = Role.Button,
                        onClick = onClick
                    )
                } else {
                    it
                }
            },
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        ProvideTextStyle(
            textStyle.takeIf { it.color.isSpecified }
                ?: textStyle.copy(color = contentColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = content,
            )
        }
    }
}

@Composable
internal fun FDialogDivider(
    color: Color,
    thickness: Dp? = null,
    horizontal: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val thickness = thickness ?: with(LocalDensity.current) { 1f.toDp() }
    if (horizontal) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(thickness)
                .background(color = color)
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(thickness)
                .background(color = color)
        )
    }
}
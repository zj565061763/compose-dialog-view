package com.sd.lib.compose.dialogview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
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
internal fun LibDialogButton(
    modifier: Modifier = Modifier,
    contentColor: Color,
    textStyle: TextStyle,
    onClick: (() -> Unit)?,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .heightIn(50.dp)
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
        color = Color.Transparent,
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
internal fun LibDialogDivider(
    modifier: Modifier = Modifier,
    color: Color,
    thickness: Dp = with(LocalDensity.current) { 1f.toDp() },
    isHorizontal: Boolean = true,
) {
    Box(
        modifier = modifier
            .background(color = color)
            .let {
                if (isHorizontal) {
                    it
                        .fillMaxWidth()
                        .height(thickness)
                } else {
                    it
                        .fillMaxHeight()
                        .width(thickness)
                }
            }
    )
}
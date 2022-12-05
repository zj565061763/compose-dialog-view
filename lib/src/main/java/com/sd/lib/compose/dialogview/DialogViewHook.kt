package com.sd.lib.compose.dialogview

import androidx.compose.runtime.Composable
import com.sd.lib.vdialog.IDialog

object DialogViewHook {
    /** 确认View参数拦截 */
    var confirmViewParamsHook: ((FDialogConfirmViewParams) -> Unit)? = null

    /** [setComposable]拦截 */
    var setComposableHook: (@Composable (content: (@Composable () -> Unit), dialog: IDialog) -> Unit)? = null
}
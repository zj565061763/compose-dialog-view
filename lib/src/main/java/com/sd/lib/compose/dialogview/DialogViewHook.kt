package com.sd.lib.compose.dialogview

object DialogViewHook {
    /** 确认View参数拦截 */
    var confirmViewParamsHook: ((FDialogConfirmViewParams) -> Unit)? = null
}
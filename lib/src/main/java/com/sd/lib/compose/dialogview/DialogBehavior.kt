package com.sd.lib.compose.dialogview

/**
 * 窗口行为
 */
object DialogBehavior {
    internal var confirm: (FDialogConfirm.() -> Unit)? = null
        private set

    internal var menu: (FDialogMenu<*>.() -> Unit)? = null
        private set

    internal var progress: (FDialogProgress.() -> Unit)? = null
        private set

    /**
     * 配置确认窗口行为
     */
    @JvmStatic
    fun confirm(block: FDialogConfirm.() -> Unit) {
        this.confirm = block
    }

    /**
     * 配置菜单窗口行为
     */
    @JvmStatic
    fun menu(block: FDialogMenu<*>.() -> Unit) {
        this.menu = block
    }

    /**
     * 配置加载窗口行为
     */
    @JvmStatic
    fun progress(block: FDialogProgress.() -> Unit) {
        this.progress = block
    }
}
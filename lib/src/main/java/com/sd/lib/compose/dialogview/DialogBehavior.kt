package com.sd.lib.compose.dialogview

/**
 * 窗口行为
 */
object DialogBehavior {
    internal var loading: (FDialogLoading.() -> Unit)? = null
        private set

    internal var confirm: (FDialogConfirm.() -> Unit)? = null
        private set

    internal var menu: (FDialogMenu<*>.() -> Unit)? = null
        private set

    /**
     * 配置加载窗口行为
     */
    @JvmStatic
    fun loading(block: FDialogLoading.() -> Unit) {
        this.loading = block
    }

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
}
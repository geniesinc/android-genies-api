package com.genies.avatar_api_sampleapp.util

import com.genies.avatar_api_sampleapp.R

interface ToolbarHost {
    fun updateToolbar(title: String, action: ToolbarAction = ToolbarBackAction())
    fun hideToolbar()
}

sealed class ToolbarAction
data class ToolbarBackAction(val iconRes: Int = R.drawable.ic_baseline_arrow_back_24): ToolbarAction()
object ToolbarNoAction : ToolbarAction()
data class ToolbarCustomAction(val iconRes: Int, val action: () -> Unit) : ToolbarAction()
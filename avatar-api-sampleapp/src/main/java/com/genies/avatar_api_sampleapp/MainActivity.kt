package com.genies.avatar_api_sampleapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.genies.avatar_api_sampleapp.util.*
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ToolbarHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWindow()

        main_container.background = getBackgroundGradient()
        mainAppBar.elevation = 0f

    }

    override fun updateToolbar(title: String, toolbarAction: ToolbarAction) {
        mainAppBar.visibility = View.VISIBLE
        mainAppBar.title = title
        when(toolbarAction) {
            is ToolbarNoAction -> {
                mainAppBar.navigationIcon = null
                mainAppBar.setNavigationOnClickListener(null)
            }
            is ToolbarBackAction -> {
                mainAppBar.setNavigationIcon(toolbarAction.iconRes)
                mainAppBar.setNavigationOnClickListener {
                    findNavController(R.id.nav_host_fragment).popBackStack()
                }
            }
            is ToolbarCustomAction -> {
                mainAppBar.setNavigationIcon(toolbarAction.iconRes)
                mainAppBar.setNavigationOnClickListener {
                    toolbarAction.action()
                }
            }
        }
    }

    override fun hideToolbar() {
        mainAppBar.visibility = View.GONE
    }

    private fun initWindow() {
        // Set view to be edge to edge and request light status bar and nav bars
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        Insetter.builder() // This will apply the system window insets as padding to left, bottom and right of the view,
            // maintaining the original padding (from the layout XML, style, etc)
            .applySystemWindowInsetsToPadding(Side.LEFT or Side.BOTTOM or Side.RIGHT or Side.TOP) // This is a shortcut for view.setOnApplyWindowInsetsListener(builder.build())
            .applyToView(main_container)
    }

}
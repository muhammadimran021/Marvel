package org.saulmm.marvel

import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import org.saulmm.marvel.app.utils.ext.isDarkMode

fun Window.applyEdgeToEdge() {
    val isLightBackground = !(context.isDarkMode ?: false)

    val lightStatusBarFlag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.takeIf {
        isLightBackground
    } ?: 0

    val lightNavBarFlag = if (isLightBackground && VERSION.SDK_INT >= VERSION_CODES.O) {
        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    } else {
        0
    }

    val systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        lightStatusBarFlag or
        lightNavBarFlag

    navigationBarColor = Color.TRANSPARENT
    statusBarColor = Color.TRANSPARENT
    decorView.systemUiVisibility = systemUiVisibility
}
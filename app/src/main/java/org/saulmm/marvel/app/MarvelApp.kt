package org.saulmm.marvel.app

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

/**
 * When using Hilt, the Application class must
 * be annotated with @HiltAndroidApp for kick
 * off the code generation.
 */
@HiltAndroidApp
class MarvelApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}
package org.saulmm.marvel

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * When using Hilt, the Application class must
 * be annotated with @HiltAndroidApp for kick
 * off the code generation.
 */
@HiltAndroidApp
class MarvelApp: Application() {
}
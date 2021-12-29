package org.saulmm.marvel.app.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import org.saulmm.marvel.BuildConfig

@GlideModule
class GlideConfigurationModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.apply {
            setDefaultTransitionOptions(Drawable::class.java, DrawableTransitionOptions().crossFade())
            setDefaultTransitionOptions(Bitmap::class.java, BitmapTransitionOptions().crossFade())
            setLogLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.ERROR)
        }
    }
}

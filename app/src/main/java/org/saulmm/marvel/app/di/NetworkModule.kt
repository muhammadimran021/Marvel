package org.saulmm.marvel.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.saulmm.marvel.BuildConfig
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val READ_TIMEOUT_LIMIT_MINS = 2L
    private const val CONNECT_TIMEOUT_LIMIT_MINS = 3L

    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when (BuildConfig.BUILD_TYPE) {
                "debug" -> HttpLoggingInterceptor.Level.HEADERS
                "release" -> HttpLoggingInterceptor.Level.NONE
                else -> throw IllegalStateException("Unknown build type ${BuildConfig.BUILD_TYPE}")
            }
        }
    }

    @Provides
    fun provideBaseOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(CONNECT_TIMEOUT_LIMIT_MINS, TimeUnit.MINUTES)
            .connectTimeout(READ_TIMEOUT_LIMIT_MINS, TimeUnit.MINUTES)
    }
}
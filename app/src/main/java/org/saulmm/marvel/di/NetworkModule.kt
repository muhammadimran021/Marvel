package org.saulmm.marvel.di

import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.saulmm.marvel.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

class NetworkModule {

    companion object {
        const val READ_TIMEOUT_LIMIT_MINS = 2
    }

    @Provides
    @Named("endpoint")
    fun provideEndpoint(): String {
        return "http://hello.com"
    }

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
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()
    }
}
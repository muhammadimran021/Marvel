package org.saulmm.marvel.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object EndpointModule {

    @Provides
    @Named("endpoint")
    fun provideEndpoint(): String {
        return "https://gateway.marvel.com"
    }

    @Provides
    fun provideEndpoints(): List<String> {
        return listOf(
            "http://gateway.marvel.com",
            "https://gateway.marvel.com"
        )
    }
}
package org.saulmm.marvel.characters.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.saulmm.marvel.BuildConfig
import org.saulmm.marvel.characters.data.CharacterDatasource
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.remote.CharacterRemoteDatasource
import org.saulmm.marvel.characters.data.remote.api.MarvelApiService
import org.saulmm.marvel.characters.data.remote.api.MarvelApiServiceAuthenticatorInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharactersModule {

    @Provides
    @Singleton
    fun providesRepository(remote: CharacterRemoteDatasource): CharacterRepository {
        return CharacterRepository(remote = remote)
    }

    @Provides
    @Singleton
    fun provideApiAuthenticator(): MarvelApiServiceAuthenticatorInterceptor {
        return MarvelApiServiceAuthenticatorInterceptor(
            publicKey = BuildConfig.MARVEL_PUBLIC_KEY,
            privateKey = BuildConfig.MARVEL_PRIVATE_KEY
        )
    }

    @Provides
    @Singleton
    fun provideMarvelApiService(
        @Named("endpoint") endPoint: String,
        apiAuthenticator: MarvelApiServiceAuthenticatorInterceptor,
        clientBuilder: OkHttpClient.Builder
    ): MarvelApiService {
        val client = clientBuilder.addInterceptor(apiAuthenticator)
            .build()

        val builder = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .baseUrl(endPoint)

        if (BuildConfig.DEBUG)
            builder.validateEagerly(true)

        return builder.build()
            .create(MarvelApiService::class.java)
    }
}
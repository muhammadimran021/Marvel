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
import org.saulmm.marvel.characters.data.remote.MarvelApiService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharactersModule {

    @Provides
    @Singleton
    fun providesRepository(): CharacterRepository {
        return CharacterRepository(
            remote = object : CharacterDatasource {
                override suspend fun characters(): List<Character> {
                    TODO("Not yet implemented")
                }

                override suspend fun character(id: Int): Character? {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideMarvelApiClient(
        @Named("endpoint") endPoint: String,
        client: OkHttpClient
    ): MarvelApiService {
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
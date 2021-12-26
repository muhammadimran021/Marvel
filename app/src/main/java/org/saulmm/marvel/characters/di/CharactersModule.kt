package org.saulmm.marvel.characters.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.saulmm.marvel.characters.data.CharacterDatasource
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.data.models.Character
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
}
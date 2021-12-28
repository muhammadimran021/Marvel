package org.saulmm.marvel.characters.data.remote

import org.saulmm.marvel.characters.data.CharacterDatasource
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.characters.data.remote.api.MarvelApiService
import org.saulmm.marvel.characters.data.utils.toCharacter
import javax.inject.Inject

class CharacterRemoteDatasource @Inject constructor(
    val apiService: MarvelApiService
): CharacterDatasource {
    override suspend fun characters(): List<CharacterPreview> {
        return apiService.characters().pagedResults.results.map { it.toCharacter() }
    }

    override suspend fun character(id: Int): CharacterPreview? {
        TODO("Not yet implemented")
    }
}
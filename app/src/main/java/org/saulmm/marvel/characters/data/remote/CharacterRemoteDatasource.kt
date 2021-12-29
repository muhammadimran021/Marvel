package org.saulmm.marvel.characters.data.remote

import kotlinx.coroutines.*
import org.saulmm.marvel.characters.data.CharacterDatasource
import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.characters.data.remote.api.MarvelApiService
import org.saulmm.marvel.characters.data.remote.models.CharacterOrderDto
import org.saulmm.marvel.characters.data.utils.toCharacter
import org.saulmm.marvel.characters.data.utils.toComic
import org.saulmm.marvel.di.IoDispatcher
import javax.inject.Inject

class CharacterRemoteDatasource @Inject constructor(
    private val apiService: MarvelApiService,
    @IoDispatcher private val io: CoroutineDispatcher
): CharacterDatasource {
    override suspend fun characters(offset: Int): List<CharacterPreview> {
        return apiService.characters(
            offset = offset,
            orderBy = CharacterOrderDto.MODIFIED.value
        ).pagedResults.results.map { it.toCharacter() }
    }

    override suspend fun character(id: Int): Character? {
        return withContext(io) {
            val characterDetail = apiService.characterDetail(id).pagedResults.results.firstOrNull()
                ?: return@withContext null

            val comicDetailsRequests = characterDetail.comics.items
                .mapNotNull { it.resourceUri }
                .map { async { apiService.comic(it) }
            }

            val comics = comicDetailsRequests.awaitAll()
                .mapNotNull { it.pagedResults.results.firstOrNull() }
                .map { it.toComic() }

            Character(id, characterDetail.name, comics)
        }
    }
}
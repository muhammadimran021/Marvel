package org.saulmm.marvel.characters.data.remote

import kotlinx.coroutines.*
import org.saulmm.marvel.characters.data.CharacterDatasource
import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.data.remote.api.MarvelApiService
import org.saulmm.marvel.characters.data.remote.models.CharacterItemDto
import org.saulmm.marvel.characters.data.remote.models.CharacterOrderDto
import org.saulmm.marvel.characters.data.remote.models.CharacterPreviewDto
import org.saulmm.marvel.characters.data.utils.toCharacterPreview
import org.saulmm.marvel.characters.data.utils.toComic
import org.saulmm.marvel.app.di.IoDispatcher
import org.saulmm.marvel.characters.data.utils.toImage
import javax.inject.Inject

class CharacterRemoteDatasource @Inject constructor(
    private val apiService: MarvelApiService,
    @IoDispatcher private val io: CoroutineDispatcher
): CharacterDatasource {
    override suspend fun characters(offset: Int): List<CharacterPreview> {
        return apiService.characters(
            offset = offset,
            orderBy = CharacterOrderDto.MODIFIED.value
        ).pagedResults.results.map(CharacterPreviewDto::toCharacterPreview)
    }

    override suspend fun character(id: Int, comicsLimit: Int): Character? {
        return withContext(io) {
            val characterDetail = apiService.characterDetail(id).pagedResults.results.firstOrNull()
                ?: return@withContext null

            // Map every comic resource URI to a request to be run in parallel
            val comicDetailsRequests = characterDetail.comics.items
                .take(comicsLimit)
                .mapNotNull(CharacterItemDto::resourceUri)
                .map { async { apiService.comic(it) }
            }

            val comics = comicDetailsRequests.awaitAll()
                .mapNotNull { it.pagedResults.results.firstOrNull() }
                .map { it.toComic() }

            Character(
                id = id,
                name = characterDetail.name,
                image = characterDetail.thumbnail.toImage(),
                comics = comics
            )
        }
    }
}
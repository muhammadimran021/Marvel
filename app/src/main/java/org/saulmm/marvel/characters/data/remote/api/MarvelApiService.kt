package org.saulmm.marvel.characters.data.remote.api

import org.saulmm.marvel.characters.data.remote.models.CharacterDetailDto
import org.saulmm.marvel.characters.data.remote.models.CharacterPreviewDto
import org.saulmm.marvel.characters.data.remote.models.ComicDto
import org.saulmm.marvel.characters.data.remote.models.MarvelApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MarvelApiService {

    @GET("/v1/public/characters")
    suspend fun characters(): MarvelApiResponseDto<CharacterPreviewDto>

    @GET("/v1/public/characters")
    suspend fun characterDetail(
        @Query("id") id: Int
    ): MarvelApiResponseDto<CharacterDetailDto>


    @GET
    suspend fun comic(@Url url: String): MarvelApiResponseDto<ComicDto>
}
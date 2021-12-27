package org.saulmm.marvel.characters.data.remote.api

import org.saulmm.marvel.characters.data.remote.CharacterDto
import org.saulmm.marvel.characters.data.remote.MarvelApiResponseDto
import retrofit2.http.GET

interface MarvelApiService {

    @GET("/v1/public/characters")
    suspend fun characters(): MarvelApiResponseDto<CharacterDto>
}
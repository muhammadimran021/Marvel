package org.saulmm.marvel.characters.data.remote.models

import com.squareup.moshi.Json

data class CharacterItemDto(
    @field:Json(name = "resourceURI")
    val resourceUri: String?,
    val name: String
)
package org.saulmm.marvel.characters.data.remote.models

import com.squareup.moshi.Json

data class MarvelApiResponseDto<T>(
    @field:Json(name = "data")
    val pagedResults: PagedDataDto<T>
)
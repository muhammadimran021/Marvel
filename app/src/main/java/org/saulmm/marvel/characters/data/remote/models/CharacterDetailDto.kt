package org.saulmm.marvel.characters.data.remote.models

import org.saulmm.marvel.characters.domain.models.Image

data class CharacterDetailDto(
    val id: Int,
    val name: String,
    val thumbnail: ImageDto,
    val comics: CollectionDto
)
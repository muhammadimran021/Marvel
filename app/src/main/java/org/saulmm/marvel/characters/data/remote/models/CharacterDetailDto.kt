package org.saulmm.marvel.characters.data.remote.models

data class CharacterDetailDto(
    val id: Int,
    val name: String,
    val comics: CollectionDto
)
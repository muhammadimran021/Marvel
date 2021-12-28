package org.saulmm.marvel.characters.data.remote.models

data class ComicDto(
    val id: Int,
    val title: String,
    val textObjects: List<TextObjectDto>,
    val images: List<ImageDto>
)
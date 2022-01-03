package org.saulmm.marvel.characters.domain.models

data class Comic(
    val id: Int,
    val title: String,
    val previewText: String?,
    val images: List<Image>
)
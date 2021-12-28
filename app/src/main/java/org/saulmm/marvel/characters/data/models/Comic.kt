package org.saulmm.marvel.characters.data.models

data class Comic(
    val id: Int,
    val title: String,
    val previewText: String?,
    val images: List<Image>
)
package org.saulmm.marvel.characters.domain.models

data class Character(
    val id: Int,
    val name: String,
    val image: Image,
    val comics: List<Comic>
)

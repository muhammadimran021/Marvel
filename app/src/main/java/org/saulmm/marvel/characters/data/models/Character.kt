package org.saulmm.marvel.characters.data.models

data class Character(
    val id: Int,
    val name: String,
    val comics: List<Comic>
)

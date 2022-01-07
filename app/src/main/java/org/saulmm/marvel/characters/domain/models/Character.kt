package org.saulmm.marvel.characters.domain.models

data class Character(
    val id: Int,
    val name: String,
    val comics: List<Comic>
)

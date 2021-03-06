package org.saulmm.marvel.characters.domain.models

import java.io.Serializable

data class CharacterPreview(
    val id: Int,
    val name: String,
    val image: Image
): Serializable
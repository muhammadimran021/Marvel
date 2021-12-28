package org.saulmm.marvel.characters.data.utils

import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.characters.data.remote.CharacterDto

fun CharacterDto.toCharacter(): CharacterPreview {
    return CharacterPreview(
        id = id,
        name = name
    )
}
package org.saulmm.marvel.characters.data.utils

import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.remote.CharacterDto

fun CharacterDto.toCharacter(): Character {
    return Character(
        id = id,
        name = name
    )
}
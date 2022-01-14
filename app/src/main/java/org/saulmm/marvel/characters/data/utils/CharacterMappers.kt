package org.saulmm.marvel.characters.data.utils

import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.data.remote.models.CharacterPreviewDto
import org.saulmm.marvel.characters.domain.models.Character

fun CharacterPreviewDto.toCharacterPreview(): CharacterPreview {
    return CharacterPreview(
        id = id,
        name = name,
        image = thumbnail.toImage()
    )
}

fun Character.toCharacterPreview(): CharacterPreview {
    return CharacterPreview(
        id = id,
        name = name,
        image = image
    )
}
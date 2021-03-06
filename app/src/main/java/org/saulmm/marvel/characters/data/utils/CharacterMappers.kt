package org.saulmm.marvel.characters.data.utils

import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.data.remote.models.CharacterPreviewDto

fun CharacterPreviewDto.toCharacter(): CharacterPreview {
    return CharacterPreview(
        id = id,
        name = name,
        image = thumbnail.toImage()
    )
}
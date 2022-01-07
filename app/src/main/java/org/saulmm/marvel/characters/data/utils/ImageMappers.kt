package org.saulmm.marvel.characters.data.utils

import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.characters.data.remote.models.ImageDto

fun ImageDto.toImage(): Image {
    return Image(path, extension)
}
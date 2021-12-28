package org.saulmm.marvel.characters.data.utils

import org.saulmm.marvel.characters.data.models.Comic
import org.saulmm.marvel.characters.data.remote.models.ComicDto
import org.saulmm.marvel.characters.data.remote.models.ImageDto

fun ComicDto.toComic(): Comic {
    return Comic(
        id = id,
        title = title,
        previewText = textObjects.firstOrNull()?.text,
        images = images.map(ImageDto::toImage)
    )
}
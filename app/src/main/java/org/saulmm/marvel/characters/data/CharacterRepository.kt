package org.saulmm.marvel.characters.data

import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview

class CharacterRepository(
    val remote: CharacterDatasource
) {
    private companion object {
        const val COMICS_PER_CHARACTER = 4
    }

    suspend fun characters(offset: Int): List<CharacterPreview> {
        return remote.characters(offset)
    }

    suspend fun character(id: Int): Character? {
        return remote.character(id = id, comicsLimit = COMICS_PER_CHARACTER)
    }
}
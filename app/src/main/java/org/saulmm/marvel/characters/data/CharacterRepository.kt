package org.saulmm.marvel.characters.data

import kotlinx.coroutines.delay
import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.models.CharacterPreview

class CharacterRepository(
    val remote: CharacterDatasource
) {

    suspend fun characters(offset: Int): List<CharacterPreview> {
        return remote.characters(offset)
    }

    suspend fun character(id: Int): Character? {
        return remote.character(id)
    }
}
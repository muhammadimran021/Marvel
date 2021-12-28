package org.saulmm.marvel.characters.data

import org.saulmm.marvel.characters.data.models.CharacterPreview

class CharacterRepository(
    val remote: CharacterDatasource
) {

    suspend fun characters(): List<CharacterPreview> {
        return remote.characters()
    }

    suspend fun character(id: Int): CharacterPreview? {
        TODO()
    }
}
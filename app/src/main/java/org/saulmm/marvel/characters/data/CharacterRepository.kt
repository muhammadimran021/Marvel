package org.saulmm.marvel.characters.data

import org.saulmm.marvel.characters.data.models.Character

class CharacterRepository(
    val remote: CharacterDatasource
) {

    suspend fun characters(): List<Character> {
        TODO()
    }

    suspend fun character(id: Int): Character? {
        TODO()
    }
}
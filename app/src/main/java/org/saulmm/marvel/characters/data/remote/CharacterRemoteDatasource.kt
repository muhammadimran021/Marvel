package org.saulmm.marvel.characters.data.remote

import org.saulmm.marvel.characters.data.CharacterDatasource
import org.saulmm.marvel.characters.data.models.Character

class CharacterRemoteDatasource: CharacterDatasource {
    override suspend fun characters(): List<Character> {
        TODO("Not yet implemented")
    }

    override suspend fun character(id: Int): Character? {
        TODO("Not yet implemented")
    }
}
package org.saulmm.marvel.characters.data

import org.saulmm.marvel.characters.data.models.Character

interface CharacterDatasource {
    suspend fun characters(): List<Character>

    suspend fun character(id: Int): Character?
}
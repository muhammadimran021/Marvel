package org.saulmm.marvel.characters.data

import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.models.CharacterPreview

interface CharacterDatasource {
    suspend fun characters(): List<CharacterPreview>

    suspend fun character(id: Int): Character?
}
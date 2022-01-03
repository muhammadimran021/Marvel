package org.saulmm.marvel.characters.data

import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview

interface CharacterDatasource {
    suspend fun characters(offset: Int): List<CharacterPreview>

    suspend fun character(id: Int): Character?
}
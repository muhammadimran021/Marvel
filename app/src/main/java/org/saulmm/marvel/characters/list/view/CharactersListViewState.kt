package org.saulmm.marvel.characters.list.view

import org.saulmm.marvel.characters.domain.models.CharacterPreview

sealed class CharactersListViewState {
    object Loading: CharactersListViewState()
    class Failure(e: Throwable): CharactersListViewState()
    class Success(val characters: List<CharacterPreview>): CharactersListViewState()
}
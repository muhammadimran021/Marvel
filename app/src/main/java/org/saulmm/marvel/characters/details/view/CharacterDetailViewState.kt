package org.saulmm.marvel.characters.details.view

import org.saulmm.marvel.characters.domain.models.Character

sealed class CharacterDetailViewState {
    object Loading: CharacterDetailViewState()
    class Failure(val e: Throwable): CharacterDetailViewState()
    class Success(val character: Character): CharacterDetailViewState()
}
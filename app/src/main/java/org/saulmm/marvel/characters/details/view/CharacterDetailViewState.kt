package org.saulmm.marvel.characters.details.view

import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview

sealed class CharacterDetailViewState {
    class LoadingWithPreview(val preview: CharacterPreview): CharacterDetailViewState()
    class Success(val character: Character): CharacterDetailViewState()
    class Failure(val e: Throwable): CharacterDetailViewState()
}
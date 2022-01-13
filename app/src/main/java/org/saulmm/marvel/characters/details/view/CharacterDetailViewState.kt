package org.saulmm.marvel.characters.details.view

import org.saulmm.marvel.characters.data.utils.toCharacterPreview
import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview

sealed class CharacterDetailViewState(open val preview: CharacterPreview) {
    class LoadingWithPreview(override val preview: CharacterPreview)
        : CharacterDetailViewState(preview)

    class Success(val character: Character)
        : CharacterDetailViewState(character.toCharacterPreview())

    class Failure(
        override val preview: CharacterPreview,
        val e: Throwable
    ): CharacterDetailViewState(preview)
}
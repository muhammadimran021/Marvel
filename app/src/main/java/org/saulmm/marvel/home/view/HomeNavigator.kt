package org.saulmm.marvel.home.view

import org.saulmm.marvel.characters.domain.models.CharacterPreview

interface HomeNavigator {

    fun showCharactersList()

    fun showCharacterDetail(characterPreview: CharacterPreview)
}
package org.saulmm.marvel.home.view

import org.saulmm.marvel.characters.data.models.CharacterPreview

interface HomeNavigator {

    fun showCharactersList()

    fun showCharacterDetail(characterPreview: CharacterPreview)
}
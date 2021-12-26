package org.saulmm.marvel.characters.details.view

import androidx.fragment.app.Fragment
import org.saulmm.marvel.R
import org.saulmm.marvel.databinding.FragmentCharacterDetailBinding
import org.saulmm.marvel.utils.viewBinding

class CharacterDetailFragment: Fragment(
    R.layout.fragment_character_detail
) {
    private val binding by viewBinding(FragmentCharacterDetailBinding::bind)
}
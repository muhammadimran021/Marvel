package org.saulmm.marvel.characters.list.view

import androidx.fragment.app.Fragment
import org.saulmm.marvel.R
import org.saulmm.marvel.databinding.FragmentCharacterListBinding
import org.saulmm.marvel.utils.viewBinding

class CharacterListFragment: Fragment(
    R.layout.fragment_character_list
) {
    private val binding by viewBinding(FragmentCharacterListBinding::bind)
}
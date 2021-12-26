package org.saulmm.marvel.characters.list.view

import androidx.fragment.app.Fragment
import org.saulmm.marvel.R
import org.saulmm.marvel.databinding.FragmentCharacterListBinding
import org.saulmm.marvel.utils.ext.viewBinding

class CharacterListFragment: Fragment(R.layout.fragment_character_list) {
    companion object {
        fun newInstance(): CharacterListFragment {
            return CharacterListFragment()
        }
    }

    private val binding by viewBinding(FragmentCharacterListBinding::bind)
}
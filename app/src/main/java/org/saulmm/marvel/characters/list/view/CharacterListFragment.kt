package org.saulmm.marvel.characters.list.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.saulmm.marvel.R
import org.saulmm.marvel.databinding.FragmentCharacterListBinding
import org.saulmm.marvel.utils.ext.viewBinding

@AndroidEntryPoint
class CharacterListFragment: Fragment(R.layout.fragment_character_list) {
    companion object {
        fun newInstance(): CharacterListFragment {
            return CharacterListFragment()
        }
    }

    private val binding by viewBinding(FragmentCharacterListBinding::bind)
    private val viewModel: CharacterListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "Viewmodel here: $viewModel", Toast.LENGTH_SHORT).show()
    }
}
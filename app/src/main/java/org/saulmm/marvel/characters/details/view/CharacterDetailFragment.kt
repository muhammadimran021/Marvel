package org.saulmm.marvel.characters.details.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.saulmm.marvel.R
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.databinding.FragmentCharacterDetailBinding
import org.saulmm.marvel.utils.ext.argument
import org.saulmm.marvel.utils.ext.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment: Fragment(R.layout.fragment_character_detail) {
    companion object {
        const val EXTRA_CHARACTER_PREVIEW = "extra_character_preview"

        fun newInstance(characterPreview: CharacterPreview): CharacterDetailFragment {
            return CharacterDetailFragment().apply {
                arguments = bundleOf(EXTRA_CHARACTER_PREVIEW to characterPreview)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: CharacterDetailViewModel.Factory

    private val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    private val characterPreview: CharacterPreview by argument(EXTRA_CHARACTER_PREVIEW)

    private val viewModel: CharacterDetailViewModel by viewModels {
        CharacterDetailViewModel.provideFactory(viewModelFactory, characterPreview)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.txtTitle.text = characterPreview.name

        Glide.with(requireContext())
            .load(characterPreview.image.landScapeIncredible)
            .into(binding.imgCharacter)
    }

    private fun setupObservers() {
        viewModel.loadCharacterDetail()
    }
}
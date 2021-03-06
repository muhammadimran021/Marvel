package org.saulmm.marvel.characters.details.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.saulmm.marvel.R
import org.saulmm.marvel.app.utils.Html
import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Comic
import org.saulmm.marvel.characters.details.view.CharacterDetailViewState.*
import org.saulmm.marvel.databinding.FragmentCharacterDetailBinding
import org.saulmm.marvel.app.utils.ext.argument
import org.saulmm.marvel.app.utils.ext.launchAndRepeatWithViewLifecycle
import org.saulmm.marvel.app.utils.ext.viewBinding
import org.saulmm.marvel.databinding.DialogComicDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    companion object {
        const val EXTRA_CHARACTER_PREVIEW = "extra_character_preview"
        const val TAG = "CharacterDetailFragment"

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
    private val comicsAdapter: ComicsAdapter by lazy {
        ComicsAdapter(
            layoutInflater,
            ::onComicClick
        )
    }

    private val viewModel: CharacterDetailViewModel by viewModels {
        CharacterDetailViewModel.provideFactory(viewModelFactory, characterPreview)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    private fun setupView() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        binding.containerCollapsing.title = characterPreview.name
        binding.txtTitle.text = characterPreview.name
        binding.recyclerComics.adapter = comicsAdapter
        binding.viewError.btnTryAgain.setOnClickListener {
            viewModel.tryAgainAction?.invoke()
        }

        Glide.with(requireContext())
            .load(characterPreview.image.landScapeIncredible)
            .into(binding.imgCharacter)
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.onViewState
                    .filterNotNull()
                    .collect(::bindViewState)
            }
        }
    }

    private fun bindViewState(viewState: CharacterDetailViewState) {
        when (viewState) {
            Loading -> {
                showFailure(false)
                showLoading(true)
            }
            is Failure -> {
                showFailure(true)
                showLoading(false)
            }
            is Success -> {
                showFailure(false)
                showLoading(false)
                bindCharacter(viewState.character)
            }
        }
    }

    private fun showFailure(show: Boolean) {
        binding.viewError.root.isVisible = show
    }

    private fun bindCharacter(character: Character) {
        binding.recyclerComics.isVisible = character.comics.isNotEmpty()
        binding.viewEmpty.root.isVisible = character.comics.isEmpty()
        comicsAdapter.submitList(character.comics)
    }

    private fun showLoading(show: Boolean) {
        binding.viewLoadingComics.root.isVisible = show
    }

    private fun onComicClick(comic: Comic) {
        val comicBinding = DialogComicDetailBinding
            .inflate(layoutInflater, null, false)

        comic.images.randomOrNull()?.let {
            Glide.with(binding.root)
                .load(it.portraitIncredible)
                .into(comicBinding.imgComic)
        }

        comic.previewText?.takeIf { it.isNotEmpty() }?.let {
            comicBinding.txtParagraph.text = Html.fromHtml(it)
        }

        comicBinding.txtTitle.text = comic.title

        MaterialAlertDialogBuilder(requireContext())
            .setView(comicBinding.root)
            .show()
    }
}
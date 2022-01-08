package org.saulmm.marvel.characters.list.view

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.saulmm.marvel.R
import org.saulmm.marvel.app.utils.ext.*
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.databinding.FragmentCharacterListBinding
import org.saulmm.marvel.home.view.HomeNavigator

@AndroidEntryPoint
class CharacterListFragment: Fragment(R.layout.fragment_character_list) {
    companion object {
        const val TAG = "CharacterListFragment"
        fun newInstance(): CharacterListFragment {
            return CharacterListFragment()
        }
    }

    private val binding by viewBinding(FragmentCharacterListBinding::bind)
    private val viewModel: CharacterListViewModel by viewModels()
    private val charactersAdapter by lazy { CharactersAdapter(layoutInflater, ::onCharacterClick) }

    private val loadingMoreSnackBar by lazy {
        Snackbar.make(binding.root, getString(R.string.msg_loading_more), Snackbar.LENGTH_INDEFINITE)
    }

    private val errorSnackBar by lazy {
        Snackbar.make(binding.root, getString(R.string.label_something_bad_happened), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_try_again) { onTryAgain() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupView()
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

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.fabGoUp.updateMargins(bottom = 16.dp.toInt() + systemBars.bottom)
            insets
        }

        binding.fabGoUp.setOnClickListener {
            binding.recyclerCharacters.scrollToPosition(0)
            binding.containerAppbar.setExpanded(true)
        }

        binding.recyclerCharacters.setOnScrollChangeListener { _, _, _, _, _ ->
            val hasScrolled = binding.recyclerCharacters.canScrollVertically(-1)
            if (hasScrolled && !binding.fabGoUp.isVisible) {
                binding.fabGoUp.show()
            }

            if (!hasScrolled && binding.fabGoUp.isVisible) {
                binding.fabGoUp.hide()
            }
        }
        charactersAdapter.onEndOfListReached = { lastPosition ->
            viewModel.loadCharacters(lastPosition)
        }

        binding.recyclerCharacters.adapter = charactersAdapter
        binding.viewError.btnTryAgain.setOnClickListener { onTryAgain() }
    }

    private fun bindViewState(viewState: CharacterListViewModel.CharactersViewState) {
        when (viewState) {
            is CharacterListViewModel.CharactersViewState.Failure -> {
                showLoading(false)
                showFailure(true)
            }
            CharacterListViewModel.CharactersViewState.Loading -> {
                showFailure(false)
                showLoading(true)
            }
            is CharacterListViewModel.CharactersViewState.Success -> {
                showFailure(false)
                showLoading(false)
                showCharacters(viewState.characters)
            }
        }
    }

    private fun showCharacters(characters: List<CharacterPreview>) {
        charactersAdapter.submitList(characters)
    }

    private fun showLoading(show: Boolean) {
        if (charactersAdapter.itemCount == 0) {
            showFullLoading(show)
        } else {
            showLoadMoreLoading(show)
        }
    }

    private fun showFailure(show: Boolean) {
        if (charactersAdapter.itemCount == 0) {
            showFullError(show)
        } else {
            showSmallError(show)
        }
    }

    private fun showFullLoading(show: Boolean) {
        binding.recyclerCharacters.isScrollable = !show
        binding.viewLoading.root.isVisible = show
    }

    private fun showFullError(show: Boolean) {
        binding.recyclerCharacters.isScrollable = !show
        binding.viewError.root.isVisible = show
    }

    private fun showSmallError(show: Boolean) {
        if (show) {
            errorSnackBar.show()
        } else {
            errorSnackBar.dismiss()
        }
    }

    private fun showLoadMoreLoading(show: Boolean) {
        if (show) {
            loadingMoreSnackBar.show()
        } else {
            loadingMoreSnackBar.dismiss()
        }
    }

    private fun onCharacterClick(character: CharacterPreview) {
        showLoading(false)
        (activity as? HomeNavigator)?.showCharacterDetail(character)
    }

    private fun onTryAgain() {
        viewModel.tryAgainAction?.invoke()
    }
}
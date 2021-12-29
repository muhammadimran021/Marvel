package org.saulmm.marvel.characters.list.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.saulmm.marvel.R
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.databinding.FragmentCharacterListBinding
import org.saulmm.marvel.home.view.HomeNavigator
import org.saulmm.marvel.utils.ext.launchAndRepeatWithViewLifecycle
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
    private val adapter by lazy { CharactersAdapter(layoutInflater, ::onCharacterClick) }

    private val loadingMoreSnackBar by lazy {
        Snackbar.make(binding.root, "Loading more characters", Snackbar.LENGTH_INDEFINITE)
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
        adapter.onEndOfListReached = { lastPosition ->
            viewModel.loadCharacters(lastPosition)
        }
        binding.recyclerCharacters.adapter = adapter
    }

    private fun bindViewState(viewState: CharacterListViewModel.CharactersViewState) {
        when (viewState) {
            is CharacterListViewModel.CharactersViewState.Failure -> {
                showFailure(true)
            }
            CharacterListViewModel.CharactersViewState.Loading -> {
                showLoading(true)
            }
            is CharacterListViewModel.CharactersViewState.Success -> {
                showCharacters(viewState.characters)
            }
        }
    }

    private fun showCharacters(characters: List<CharacterPreview>) {
        showLoading(false)
        showFailure(false)
        adapter.submitList(characters)
    }

    private fun showLoading(show: Boolean) {
        if (adapter.itemCount == 0) {
            showFullLoading(show)
        } else {
            showLoadMoreLoading(show)
        }
    }

    private fun showFullLoading(show: Boolean) {
        binding.progressCharactersLoading.isVisible = show
    }

    private fun showLoadMoreLoading(show: Boolean) {
        if (show) {
            loadingMoreSnackBar.show()
        } else {
            loadingMoreSnackBar.dismiss()
        }
    }

    private fun showFailure(show: Boolean) {
        showLoading(false)
    }

    private fun onCharacterClick(character: CharacterPreview) {
        showLoading(false)
        (activity as? HomeNavigator)?.showCharacterDetail(character)
    }
}
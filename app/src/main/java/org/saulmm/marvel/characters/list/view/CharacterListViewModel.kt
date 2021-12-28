package org.saulmm.marvel.characters.list.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.data.models.Character
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
): ViewModel() {

    sealed class CharactersViewState {
        object Loading: CharactersViewState()
        class Failure(e: Throwable): CharactersViewState()
        class Success(val characters: List<Character>): CharactersViewState()
    }

    private val viewState = MutableStateFlow<CharactersViewState?>(null)
    val onViewState = viewState.asStateFlow()

    init {
        viewModelScope.launch {
            loadCharacters()
        }
    }

    private fun loadCharacters() {
        viewState.value = CharactersViewState.Loading

        viewModelScope.launch {
            runCatching { repository.characters() }
                .onFailure(::onRequestCharactersFailure)
                .onSuccess(::onRequestCharactersSuccess)
        }
    }

    private fun onRequestCharactersFailure(e: Throwable) {
        Log.e(this::class.simpleName, "onRequestCharactersFailure", e)
        viewState.value = CharactersViewState.Failure(e)
    }

    private fun onRequestCharactersSuccess(characters: List<Character>) {
        viewState.value = CharactersViewState.Success(characters)
    }
}
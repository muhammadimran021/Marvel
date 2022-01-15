package org.saulmm.marvel.characters.list.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import okhttp3.internal.toImmutableList
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
): ViewModel() {

    private val viewState = MutableStateFlow<CharactersListViewState?>(null)
    private val currentCharacters = mutableListOf<CharacterPreview>()
    val onViewState = viewState.asStateFlow()

    var tryAgainAction: (() -> Unit)? = null
        private set

    init {
        viewModelScope.launch {
            loadCharacters()
        }
    }

    fun loadCharacters(offset: Int = 0) {
        viewState.value = CharactersListViewState.Loading

        viewModelScope.launch {
            runCatching { repository.characters(offset) }
                .onFailure { onRequestCharactersFailure(it, offset) }
                .onSuccess(::onRequestCharactersSuccess)
        }
    }

    private fun onRequestCharactersFailure(e: Throwable, offset: Int) {
        logcat(LogPriority.ERROR) { "Failure with offset: $offset, $e"}
        tryAgainAction = { loadCharacters(offset) }
        viewState.value = CharactersListViewState.Failure(e)
    }

    private fun onRequestCharactersSuccess(characters: List<CharacterPreview>) {
        currentCharacters.addAll(characters)
        tryAgainAction = null
        viewState.value = CharactersListViewState.Success(currentCharacters.toImmutableList())
    }
}
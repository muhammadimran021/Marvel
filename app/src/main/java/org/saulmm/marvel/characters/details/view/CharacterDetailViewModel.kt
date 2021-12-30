package org.saulmm.marvel.characters.details.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.models.CharacterPreview
import java.lang.NullPointerException

class CharacterDetailViewModel @AssistedInject constructor(
    @Assisted val characterPreview: CharacterPreview,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            characterPreview: CharacterPreview
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(characterPreview) as T
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(characterPreview: CharacterPreview): CharacterDetailViewModel
    }

    init {
        loadCharacterDetail()
    }

    private val viewState = MutableStateFlow<CharacterDetailViewState?>(null)
    val onViewState = viewState.asStateFlow()

    var tryAgainAction: (() -> Unit)? = null
        private set

    private fun loadCharacterDetail() {
        viewModelScope.launch {
            viewState.value = CharacterDetailViewState.Loading

            runCatching { characterRepository.character(characterPreview.id) }
                .onSuccess { onCharacterDetailSuccess(it) }
                .onFailure { onCharacterDetailFailure(it) }
        }
    }

    private fun onCharacterDetailSuccess(character: Character?) {
        tryAgainAction = null
        viewState.value = if (character != null) {
            CharacterDetailViewState.Success(character)
        } else {
            CharacterDetailViewState.Failure(NullPointerException("Character is null"))
        }
    }

    private fun onCharacterDetailFailure(e: Throwable) {
        tryAgainAction = { loadCharacterDetail() }
        viewState.value = CharacterDetailViewState.Failure(e)
    }
}
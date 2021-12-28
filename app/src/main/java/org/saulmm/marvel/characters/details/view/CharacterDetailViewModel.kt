package org.saulmm.marvel.characters.details.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.data.models.CharacterPreview
import javax.inject.Inject

class CharacterDetailViewModel @AssistedInject constructor(
    @Assisted val characterPreview: CharacterPreview,
    private val characterRepository: CharacterRepository
): ViewModel() {

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
//        loadCharacterDetail()
    }

     fun loadCharacterDetail() {
        viewModelScope.launch {
            val characterDetail = characterRepository.character(characterPreview.id)
            Log.d("character", characterDetail.toString())
        }
    }
}
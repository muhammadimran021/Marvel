package org.saulmm.marvel.characters.details.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import org.saulmm.marvel.characters.data.models.CharacterPreview
import javax.inject.Inject

class CharacterDetailViewModel @AssistedInject constructor(
    @Assisted val characterPreview: CharacterPreview
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
}
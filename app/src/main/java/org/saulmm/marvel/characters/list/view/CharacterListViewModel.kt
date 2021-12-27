package org.saulmm.marvel.characters.list.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.saulmm.marvel.characters.data.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    val repository: CharacterRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            val characters = repository.characters()
            Log.i("characters", "Hello $characters")
        }
    }
}
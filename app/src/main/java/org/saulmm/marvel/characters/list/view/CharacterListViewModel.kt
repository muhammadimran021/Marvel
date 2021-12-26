package org.saulmm.marvel.characters.list.view

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.saulmm.marvel.characters.data.CharacterRepository
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    val repository: CharacterRepository
): ViewModel() {

    init {
        Log.i("di", "Hello $repository")
    }
}
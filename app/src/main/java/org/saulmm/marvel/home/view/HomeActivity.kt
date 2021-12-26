package org.saulmm.marvel.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import org.saulmm.marvel.R
import org.saulmm.marvel.characters.details.view.CharacterDetailFragment
import org.saulmm.marvel.characters.list.view.CharacterListFragment
import org.saulmm.marvel.databinding.ActivityHomeBinding
import org.saulmm.marvel.utils.ext.viewBinding

@AndroidEntryPoint
class HomeActivity : HomeNavigator, AppCompatActivity(R.layout.activity_home) {

    private val binding by viewBinding(ActivityHomeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            showCharactersList()
        }
    }

    override fun showCharactersList() {
        val characterListFragment = CharacterListFragment.newInstance()
        supportFragmentManager.commit {
            add(binding.fragmentContainer.id, characterListFragment)
        }
    }

    override fun showCharacterDetail() {
        val characterDetailFragment = CharacterDetailFragment.newInstance()
        supportFragmentManager.commit {
            add(binding.fragmentContainer.id, characterDetailFragment)
        }
    }
}
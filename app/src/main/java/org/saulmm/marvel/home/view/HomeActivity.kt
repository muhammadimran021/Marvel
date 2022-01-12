package org.saulmm.marvel.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import org.saulmm.marvel.R
import org.saulmm.marvel.applyEdgeToEdge
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.details.view.CharacterDetailFragment
import org.saulmm.marvel.characters.list.view.CharacterListFragment
import org.saulmm.marvel.databinding.ActivityHomeBinding
import org.saulmm.marvel.app.utils.ext.viewBinding
import org.saulmm.marvel.characters.details.view.CharacterDetailFragmentCompose

@AndroidEntryPoint
class HomeActivity : HomeNavigator, AppCompatActivity(R.layout.activity_home) {

    private val binding by viewBinding(ActivityHomeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.applyEdgeToEdge()

        if (savedInstanceState == null) {
            showCharactersList()
        }
    }

    override fun showCharactersList() {
        val characterListFragment = CharacterListFragment.newInstance()
        supportFragmentManager.commit {
            add(binding.fragmentContainer.id, characterListFragment, CharacterListFragment.TAG)
        }
    }

    override fun showCharacterDetail(characterPreview: CharacterPreview) {
        val detailFragment = CharacterDetailFragmentCompose.newInstance(characterPreview)

        supportFragmentManager.commit {
            replace(binding.fragmentContainer.id, detailFragment, CharacterDetailFragment.TAG)
            addToBackStack(CharacterDetailFragment.TAG)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
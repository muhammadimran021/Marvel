package org.saulmm.marvel.characters.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import org.saulmm.marvel.app.ui.MarvelTheme
import org.saulmm.marvel.app.utils.ext.argument
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Comic
import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.characters.ui.ComicUi

class CharacterDetailFragmentCompose: Fragment() {

    companion object {
        const val EXTRA_CHARACTER_PREVIEW = "extra_character_preview"

        fun newInstance(characterPreview: CharacterPreview): CharacterDetailFragmentCompose {
            return CharacterDetailFragmentCompose().apply {
                arguments = bundleOf(CharacterDetailFragment.EXTRA_CHARACTER_PREVIEW to characterPreview)
            }
        }
    }

    private val characterPreview: CharacterPreview by argument(CharacterDetailFragment.EXTRA_CHARACTER_PREVIEW)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                MaterialTheme {
                    CharacterDetailUi(characterPreview)
                }
            }
        }
    }

    @Composable
    fun CharacterDetailUi(character: CharacterPreview) {
        MarvelTheme {
            ProvideWindowInsets {
                Box(modifier = Modifier.statusBarsPadding()) {
                    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
                    val scrollBehavior = remember(decayAnimationSpec) {
                        TopAppBarDefaults.pinnedScrollBehavior()
                    }

                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = { CharacterDetailAppBar(character, scrollBehavior) },
                        content = { innerPadding -> CharacterDetailBody(innerPadding) }
                    )
                }
            }
        }
    }

    @Composable
    private fun CharacterDetailBody(innerPadding: PaddingValues) {
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val list = (0..75).map { it.toString() }
            items(count = list.size) {
                Text(
                    text = list[it],
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }

    @Composable
    private fun CharacterDetailAppBar(
        character: CharacterPreview,
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        SmallTopAppBar(
            title = {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Localized description"
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }

    @ExperimentalMaterial3Api
    @Composable
    @Preview(widthDp = 300, heightDp = 500)
    fun CharacterDetailUiPreviewDark() {
        MarvelTheme(darkTheme = true) {
            CharacterDetailUi(
                character = CharacterPreview(
                    id = 1,
                    name = "Captain America",
                    image = Image("http://google.es", ".jpg")
                )
            )
        }
    }

    @ExperimentalMaterial3Api
    @Composable
    @Preview(widthDp = 300, heightDp = 500)
    fun CharacterDetailUiPreview() {
        MarvelTheme(darkTheme = false) {
            CharacterDetailUi(
                character = CharacterPreview(
                    id = 1,
                    name = "Captain America",
                    image = Image("http://google.es", ".jpg")
                )
            )
        }
    }

    @Composable
    fun ComicDialog(comic: Comic, showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
        if (showDialog) {
            Dialog(
                onDismissRequest = { setShowDialog(false) },
            ) {
                ComicUi(comic)
            }
        }
    }
}
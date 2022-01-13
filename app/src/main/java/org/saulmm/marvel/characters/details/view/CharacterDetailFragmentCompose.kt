package org.saulmm.marvel.characters.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import org.saulmm.marvel.R
import org.saulmm.marvel.app.ui.MarvelTheme
import org.saulmm.marvel.app.utils.ext.argument
import org.saulmm.marvel.characters.data.utils.toCharacterPreview
import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Comic
import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.characters.ui.ComicUi
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragmentCompose : Fragment() {

    companion object {
        const val EXTRA_CHARACTER_PREVIEW = "extra_character_preview"

        fun newInstance(characterPreview: CharacterPreview): CharacterDetailFragmentCompose {
            return CharacterDetailFragmentCompose().apply {
                arguments = bundleOf(EXTRA_CHARACTER_PREVIEW to characterPreview)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: CharacterDetailViewModel.Factory

    private val characterPreview: CharacterPreview by argument(
        CharacterDetailFragment.EXTRA_CHARACTER_PREVIEW
    )

    private val viewModel: CharacterDetailViewModel by viewModels {
        CharacterDetailViewModel.provideFactory(viewModelFactory, characterPreview)
    }

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
                    val viewState by viewModel.onViewState.collectAsState()
                    viewState?.let { CharacterScreen(it) }
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun CharacterScreen(viewState: CharacterDetailViewState) {
        MarvelTheme {
            ProvideWindowInsets {
                Box(modifier = Modifier.statusBarsPadding()) {
                    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
                    val scrollBehavior = remember(decayAnimationSpec) {
                        TopAppBarDefaults.pinnedScrollBehavior()
                    }

                    when (viewState) {
                        is CharacterDetailViewState.Failure -> {
                            Scaffold(
                                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                                topBar = { CharacterDetailAppBar("", scrollBehavior) },
                                content = { }
                            )
                        }
                        is CharacterDetailViewState.LoadingWithPreview -> {
                            Scaffold(
                                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                                topBar = {
                                    CharacterDetailAppBar(
                                        viewState.preview.name,
                                        scrollBehavior
                                    )
                                },
                                content = { CharacterPreviewLoadingComics(viewState.preview) }
                            )
                        }
                        is CharacterDetailViewState.Success -> {
                            Scaffold(
                                modifier = Modifier
                                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                                ,
                                topBar = {
                                    CharacterDetailAppBar(
                                        viewState.character.name,
                                        scrollBehavior
                                    )
                                },
                                content = { CharacterWithComics(viewState.character) }
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun CharacterHeader(character: CharacterPreview) {
        Column {
            CharacterImage(character)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.label_marvel_character),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.label_comics),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

    }

    @Composable
    private fun CharacterImage(character: CharacterPreview) {
        GlideImage(
            contentScale = ContentScale.Crop,
            imageModel = character.image.detail,
            previewPlaceholder = R.drawable.ic_launcher_background,
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp)
        )
    }

    @Composable
    private fun ComicLoadingPlaceHolder() {
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(width = 168.dp, height = 252.dp)
                .clip(RoundedCornerShape(8.dp))
                .placeholder(
                    visible = true,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    )
                )
            ,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {}

    }

    @ExperimentalFoundationApi
    @Composable
    private fun CharacterPreviewLoadingComics(character: CharacterPreview) {
        Column {
            CharacterHeader(character = character)
            Spacer(modifier = Modifier.height(16.dp))
            repeat(times = 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ComicLoadingPlaceHolder()
                    ComicLoadingPlaceHolder()
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    private fun CharacterWithComics(character: Character) {
        val comicsPairs = character.comics.chunked(2)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            content = {
                item { CharacterHeader(character = character.toCharacterPreview()) }
                items(comicsPairs) { comicPair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        comicPair.forEach { ComicImage(it) }
                    }
                }
            }
        )
    }

    @Composable
    private fun ComicImage(comic: Comic) {
        Surface(
            modifier = Modifier.padding(4.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            GlideImage(
                contentScale = ContentScale.Fit,
                imageModel = comic.images.first().portraitIncredible,
                previewPlaceholder = R.drawable.ic_launcher_background,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colorScheme.surfaceVariant,
                    highlightColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    durationMillis = 1_000,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(width = 168.dp, height = 252.dp)
                )
            }
        }

    @Composable
    private fun lorem() {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit,"
        )
    }

    @Composable
    private fun CharacterDetailAppBar(
        appBarTitle: String,
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        SmallTopAppBar(
            title = {
                Text(
                    text = appBarTitle,
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
    @Preview(widthDp = 500, heightDp = 800)
    fun CharacterDetailUiPreviewLoading() {
        MarvelTheme() {
            CharacterScreen(
                viewState = CharacterDetailViewState.LoadingWithPreview(
                    preview = CharacterPreview(
                        id = 1,
                        name = "Captain America",
                        image = Image("http://google.es", ".jpg")
                    )
                )
            )
        }
    }

    @ExperimentalMaterial3Api
    @Composable
    @Preview(widthDp = 500, heightDp = 800)
    fun CharacterDetailUiPreviewSuccess() {
        MarvelTheme(darkTheme = true) {
            CharacterScreen(
                viewState = CharacterDetailViewState.Success(
                    character = Character(
                        id = 1,
                        name = "Captain America",
                        image = Image("http://google.es", ".jpg"),
                        comics = listOf(
                            Comic(
                                0,
                                "Avengers #1",
                                null,
                                listOf(Image("http://google.es", ".jpg"))
                            ),
                            Comic(
                                1,
                                "Avengers #2",
                                null,
                                listOf(Image("http://google.es", ".jpg"))
                            ),
                            Comic(2, "Avengers #3", null, listOf(Image("http://google.es", ".jpg")))
                        )
                    )
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
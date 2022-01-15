package org.saulmm.marvel.characters.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import org.saulmm.marvel.R
import org.saulmm.marvel.app.ui.MarvelTheme
import org.saulmm.marvel.app.utils.ext.marvelPlaceholder
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.home.view.HomeNavigator

@AndroidEntryPoint
class CharacterListFragmentCompose : Fragment() {

    companion object {
        const val LOADING_ITEMS = 5

        fun newInstance(): CharacterListFragmentCompose {
            return CharacterListFragmentCompose()
        }
    }

    private val viewModel: CharacterListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                MarvelTheme {
                    CharactersListScreen(
                        onCharacterClick = ::onCharacterClick,
                        viewmodel = viewModel
                    )
                }
            }
        }
    }

    private fun onCharacterClick(character: CharacterPreview) {
        (activity as HomeNavigator).showCharacterDetail(character)
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterItemPreview() {
    MarvelTheme {
        CharacterItem(
            characterPreview = CharacterPreview(
                id = 1,
                name = "Captain America",
                image = Image(path = "http://google.es", extension = ".jpg")
            ),
            onCharacterClick = {}
        )
    }
}

@Preview
@Composable
private fun CharacterItemPreviewDark() {
    MarvelTheme(darkTheme = true) {
        Surface(modifier = Modifier.padding(32.dp)) {
            CharacterItem(
                characterPreview = CharacterPreview(
                    id = 1,
                    name = "Captain America",
                    image = Image(path = "http://google.es", extension = ".jpg")
                ),
                onCharacterClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterItemLoadingPreview() {
    MarvelTheme {
        CharacterItemLoading()
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterItemLoadingPreviewDark() {
    MarvelTheme(darkTheme = true) {
        CharacterItemLoading()
    }
}

@Composable
fun CharactersListScreen(
    onCharacterClick: (CharacterPreview) -> Unit,
    viewmodel: CharacterListViewModel
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    val characters = viewmodel.pager.flow.collectAsLazyPagingItems()
    val loadState = characters.loadState

    ProvideWindowInsets {
        Box(modifier = Modifier.statusBarsPadding()) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = { CharactersListAppBar(scrollBehavior) },
                content = {
                    if (loadState.refresh == LoadState.Loading) {
                        CharacterListLoading()
                    } else {
                        CharactersLazyList(characters, onCharacterClick)
                    }
                }
            )
        }
    }
}

@Composable
private fun CharactersLazyList(
    characters: LazyPagingItems<CharacterPreview>,
    onCharacterClick: (CharacterPreview) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(characters) { item ->
            item?.let {
                CharacterItem(
                    characterPreview = item,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}

@Composable
private fun CharactersListAppBar(scrollBehavior: TopAppBarScrollBehavior) {
    LargeTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.label_marvel_characters),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun CharacterListLoading() {
    val loadingItems: List<String> = (1..CharacterListFragmentCompose.LOADING_ITEMS).map { it.toString() }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(loadingItems) {
            CharacterItemLoading()
        }
    }
}

@Composable
private fun CharacterItem(
    characterPreview: CharacterPreview,
    onCharacterClick: (CharacterPreview) -> Unit
) {
    CharacterItemSurface(
        modifier = Modifier.clickable { onCharacterClick(characterPreview) }
    ) {
        Row {
            CharacterItemImage(characterPreview)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = characterPreview.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(0.dp))
                Text(
                    text = stringResource(id = R.string.label_marvel_character),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun CharacterItemSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ), content = content
    )
}

@Composable
private fun CharacterItemLoading() {
    CharacterItemSurface {
        Row {
            Box(
                modifier = Modifier
                    .size(150.dp, 150.dp)
                    .marvelPlaceholder()
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
            }
        }
    }
}

@Composable
private fun CharacterItemImage(characterPreview: CharacterPreview) {
    GlideImage(
        contentScale = ContentScale.Fit,
        imageModel = characterPreview.image.squareLarge,
        previewPlaceholder = R.drawable.ic_launcher_background,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colorScheme.surfaceVariant,
            highlightColor = MaterialTheme.colorScheme.onSurfaceVariant,
            durationMillis = 1_500,
            dropOff = 0.65f,
            tilt = 20f
        ),
        modifier = Modifier
            .size(150.dp, 150.dp)
    )
}
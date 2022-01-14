package org.saulmm.marvel.characters.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.fragment.app.Fragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import org.saulmm.marvel.R
import org.saulmm.marvel.app.ui.MarvelTheme
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Image

class CharacterListFragmentCompose : Fragment() {

    companion object {
        fun newInstance(): CharacterListFragmentCompose {
            return CharacterListFragmentCompose()
        }
    }

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
                    CharactersListScreen()
                }
            }
        }
    }

    @Preview
    @Composable
    private fun CharacterItemPreview() {
        MarvelTheme {
            Surface(modifier = Modifier.padding(32.dp)) {
                CharacterItem(
                    characterPreview = CharacterPreview(
                        id = 1,
                        name = "Captain America",
                        image = Image(path = "http://google.es", extension = ".jpg")
                    )
                )
            }
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
                    )
                )
            }
        }
    }

    @Composable
    fun CharactersListScreen() {
        val decayAnimationSpec = rememberSplineBasedDecay<Float>()
        val scrollBehavior = remember(decayAnimationSpec) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
        }

        ProvideWindowInsets {
            Box(modifier = Modifier.statusBarsPadding()) {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { CharactersListAppBar(scrollBehavior) },
                    content = { ExampleList() }
                )
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
    private fun CharacterItem(characterPreview: CharacterPreview) {
        Surface(
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
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

    @Composable
    fun ExampleList() {
        val list = mutableListOf<CharacterPreview>()
        repeat(100) {
            list.add(
                CharacterPreview(
                    id = it,
                    name = "Captain America #$it",
                    image = Image(path = "http://google.es", extension = ".jpg")
                )
            )
        }

        LazyColumn {
            items(list) { item ->
                CharacterItem(characterPreview = item)
            }
        }
    }
}
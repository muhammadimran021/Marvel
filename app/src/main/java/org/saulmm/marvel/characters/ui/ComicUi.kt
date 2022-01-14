package org.saulmm.marvel.characters.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.rememberDrawablePainter
import org.saulmm.marvel.R
import org.saulmm.marvel.app.ui.MarvelTheme
import org.saulmm.marvel.characters.domain.models.Comic
import org.saulmm.marvel.characters.domain.models.Image

@Composable
fun ComicUi(comic: Comic) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            Image(
                painter = rememberDrawablePainter(ColorDrawable(Color.BLUE)),
                contentDescription = "",
                modifier = Modifier
                    .size(width = 210.dp, height = 260.dp)
                    .align(Alignment.Center)
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = comic.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comic.previewText ?: stringResource(id = R.string.placeholder_no_preview),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 10
            )
        }
//            imageModel = comic.images.first().portraitIncredible,
//        )
    }
}

@Preview(widthDp = 300, heightDp = 500)
@Composable
fun ComicPreview() {
    MarvelTheme {
        Box(
            modifier =
            Modifier.background(color = MaterialTheme.colorScheme.surface)
        ) {
            ComicUi(
                comic = Comic(
                    id = 1,
                    title = "Captain America",
                    previewText = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit",
                    images = listOf(Image(path = "http://google.es", ".jpg"))
                )
            )
        }
    }
}

@Preview(widthDp = 300, heightDp = 500)
@Composable
fun ComicDarkPreview() {
    MarvelTheme(darkTheme = true) {
        Box(
            modifier =
            Modifier.background(color = MaterialTheme.colorScheme.surface)
        ) {
            ComicUi(
                comic = Comic(
                    id = 1,
                    title = "Captain America",
                    previewText = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit",
                    images = listOf(Image(path = "http://google.es", ".jpg"))
                )
            )
        }
    }
}
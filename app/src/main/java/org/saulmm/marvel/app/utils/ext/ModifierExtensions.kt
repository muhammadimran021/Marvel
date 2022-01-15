package org.saulmm.marvel.app.utils.ext

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun Modifier.marvelPlaceholder(): Modifier {
    return this.placeholder(
        visible = true,
        color = MaterialTheme.colorScheme.surfaceVariant,
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        )
    )
}
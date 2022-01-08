package org.saulmm.marvel.characters.details.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.saulmm.marvel.characters.domain.models.Comic
import org.saulmm.marvel.databinding.ItemComicBinding

class ComicsAdapter(
    private val layoutInflater: LayoutInflater,
    private val onComicClick: (Comic) -> Unit
): ListAdapter<Comic, ComicViewHolder>(ComicDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val binding = ItemComicBinding.inflate(layoutInflater, parent, false)
        return ComicViewHolder(binding, onComicClick)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
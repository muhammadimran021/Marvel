package org.saulmm.marvel.characters.details.view

import androidx.recyclerview.widget.DiffUtil
import org.saulmm.marvel.characters.domain.models.Comic

class ComicDiffItemCallback : DiffUtil.ItemCallback<Comic>() {
    override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
        return oldItem == newItem
    }
}
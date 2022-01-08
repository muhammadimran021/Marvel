package org.saulmm.marvel.characters.list.view

import androidx.recyclerview.widget.DiffUtil
import org.saulmm.marvel.characters.domain.models.CharacterPreview

class CharacterDiffItemCallback : DiffUtil.ItemCallback<CharacterPreview?>() {
    override fun areItemsTheSame(oldItem: CharacterPreview, newItem: CharacterPreview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterPreview, newItem: CharacterPreview): Boolean {
        return oldItem == newItem
    }
}

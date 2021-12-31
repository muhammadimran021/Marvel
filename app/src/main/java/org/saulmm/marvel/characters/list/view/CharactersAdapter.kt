package org.saulmm.marvel.characters.list.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.databinding.ItemCharacterBinding

class CharactersAdapter(
    private val inflater: LayoutInflater,
    private val onCharacterClick: (CharacterPreview) -> Unit
): ListAdapter<CharacterPreview, CharacterViewHolder>(object : DiffUtil.ItemCallback<CharacterPreview?>() {
    override fun areItemsTheSame(oldItem: CharacterPreview, newItem: CharacterPreview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterPreview, newItem: CharacterPreview): Boolean {
        return oldItem == newItem
    }
}) {

    var onEndOfListReached: ((lastPosition: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(binding, onCharacterClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))

        Log.i("coordinator", "Binding item: $position")

        if (position == itemCount - 1) {
            Log.i("coordinator", "Last item reached, position = $position, itemCount - 1 = ${itemCount - 1}")
            onEndOfListReached?.invoke(position)
        }
    }
}


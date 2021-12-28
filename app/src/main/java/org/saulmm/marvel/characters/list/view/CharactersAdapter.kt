package org.saulmm.marvel.characters.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.databinding.ItemCharacterBinding

class CharactersAdapter(
    private val inflater: LayoutInflater,
    private val onCharacterClick: (Character) -> Unit
): ListAdapter<Character, CharacterViewHolder>(object : DiffUtil.ItemCallback<Character?>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(binding, onCharacterClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


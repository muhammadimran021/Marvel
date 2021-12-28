package org.saulmm.marvel.characters.list.view

import androidx.recyclerview.widget.RecyclerView
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onCharacterClick: (CharacterPreview) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(character: CharacterPreview) {
        binding.txtCharacterTitle.text = character.name
        binding.root.setOnClickListener { onCharacterClick(character) }
    }
}
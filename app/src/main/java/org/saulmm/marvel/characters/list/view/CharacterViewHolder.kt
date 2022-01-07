package org.saulmm.marvel.characters.list.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onCharacterClick: (CharacterPreview) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(character: CharacterPreview) {
        binding.txtCharacterTitle.text = character.name
        binding.root.setOnClickListener { onCharacterClick(character) }

        Glide.with(itemView.context)
            .load(character.image.squareLarge)
            .into(binding.imgCharacter)
    }
}
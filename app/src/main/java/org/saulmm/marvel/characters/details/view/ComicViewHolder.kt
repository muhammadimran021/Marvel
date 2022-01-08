package org.saulmm.marvel.characters.details.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.saulmm.marvel.characters.domain.models.Comic
import org.saulmm.marvel.databinding.ItemComicBinding

class ComicViewHolder(
    private val binding: ItemComicBinding,
    val onComicClick: (Comic) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(comic: Comic) {
        binding.root.setOnClickListener {
            onComicClick(comic)
        }

        comic.images.randomOrNull()?.let { image ->
            Glide.with(binding.root)
                .load(image.portraitIncredible)
                .into(binding.imgComic)
        }
    }
}
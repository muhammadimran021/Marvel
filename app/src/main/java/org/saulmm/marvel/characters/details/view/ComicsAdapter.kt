package org.saulmm.marvel.characters.details.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.saulmm.marvel.characters.data.models.Comic
import org.saulmm.marvel.databinding.ItemComicBinding

class ComicsAdapter(
    private val layoutInflater: LayoutInflater,
    private val onComicClick: (Comic) -> Unit
): ListAdapter<Comic, ComicViewHolder>(object : DiffUtil.ItemCallback<Comic?>() {
    override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val binding = ItemComicBinding.inflate(layoutInflater, parent, false)
        return ComicViewHolder(binding, onComicClick)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ComicViewHolder(
    val binding: ItemComicBinding,
    val onComicClick: (Comic) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(comic: Comic) {
        binding.root.setOnClickListener {
            onComicClick(comic)
        }
        Glide.with(binding.root)
            .load(comic.images.random().portraitIncredible)
            .into(binding.imgComic)
    }
}
package org.saulmm.marvel.characters.list.view

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import javax.inject.Inject

class CharactersPagingSource @Inject constructor(
    private val charactersRepository: CharacterRepository
): PagingSource<Int, CharacterPreview>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterPreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterPreview> {
        val offset = params.key ?: 0

        return try {
            val characters = charactersRepository.characters(offset = offset)

            LoadResult.Page(
                data = characters,
                prevKey = null,
                nextKey = offset + characters.size
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}
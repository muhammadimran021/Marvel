package org.saulmm.marvel.app.utils.ext

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

val <T:Any> LazyPagingItems<T>.isFullLoading: Boolean
    get() = loadState.refresh == LoadState.Loading

val <T:Any> LazyPagingItems<T>.isLoadingMore: Boolean
    get() = loadState.refresh == LoadState.Loading

val <T:Any> LazyPagingItems<T>.isFullError: Boolean
    get() = loadState.refresh is LoadState.Error
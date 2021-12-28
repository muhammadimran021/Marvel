package org.saulmm.marvel.characters.data.remote.models

data class PagedDataDto<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
)
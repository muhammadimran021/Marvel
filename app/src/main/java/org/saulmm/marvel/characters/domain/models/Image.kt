package org.saulmm.marvel.characters.domain.models

import org.saulmm.marvel.characters.data.remote.models.MarvelImageVariant
import java.io.Serializable

/**
 * An image in the marvel API is represented as the path + variant + extension.
 * Being a variant one value in [MarvelImageVariant].
 *
 * @see https://developer.marvel.com/documentation/images
 */
data class Image(
    val path: String,
    val extension: String,
): Serializable {
    val squareLarge: String
        get() = "$path/${MarvelImageVariant.SQUARE_XLARGE.value}.$extension"

    val landScapeIncredible: String
        get() = "$path/${MarvelImageVariant.LANDSCAPE_INCREDIBLE.value}.$extension"

    val portraitIncredible: String
        get() = "$path/${MarvelImageVariant.PORTRAIT_INCREDIBLE.value}.$extension"

    val portraitFantastic: String
        get() = "$path/${MarvelImageVariant.PORTRAIT_FANTASTIC.value}.$extension"

    val detail: String
        get() = "$path/${MarvelImageVariant.DETAIL.value}.$extension"
}
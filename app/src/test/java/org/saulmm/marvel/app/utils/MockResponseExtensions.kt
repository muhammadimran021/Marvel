package org.saulmm.marvel.app.utils

import okhttp3.mockwebserver.MockResponse
import java.io.InputStream

fun MockResponse.Companion.fromFile(fileName: String): MockResponse {
    val jsonStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
    val jsonBytes: ByteArray = jsonStream.readBytes()
    return MockResponse().setBody(String(jsonBytes))
}

/**
 * The JSON responses stored in the 'test/resources' folder are copies of the marvel api payloads.
 * This means that internal references to the marvel api will remain in the form (https://gateway.marvel...)
 *
 * Since there are cases where we use the retrofit with dynamic URLs, see [MarvelApiService.comic], we need to replace
 * these references with our URL from the mock web server.
 *
 * @param replaceFrom a list of terms to replace.
 * @param replaceTo the term we want to set.
 */
fun MockResponse.Companion.fromFileReplacingUrls(fileName: String, replaceFrom: List<String>, replaceTo: String): MockResponse {
    val jsonStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
    val jsonBytes: ByteArray = jsonStream.readBytes()
    val jsonString = String(jsonBytes)

    var source = jsonString
    replaceFrom.map {
        source = source.replace(it, replaceTo)
    }

    return MockResponse().setBody(source)
}
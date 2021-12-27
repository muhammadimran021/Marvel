package org.saulmm.marvel.utils

import okhttp3.mockwebserver.MockResponse
import java.io.InputStream

fun MockResponse.Companion.fromFile(fileName: String): MockResponse {
    val jsonStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
    val jsonBytes: ByteArray = jsonStream.readBytes()
    return MockResponse().setBody(String(jsonBytes))
}
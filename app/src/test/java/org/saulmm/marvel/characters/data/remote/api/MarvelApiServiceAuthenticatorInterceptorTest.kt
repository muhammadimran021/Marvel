package org.saulmm.marvel.characters.data.remote.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.saulmm.marvel.characters.di.CharactersModule
import org.saulmm.marvel.utils.ext.md5
import org.saulmm.marvel.utils.fromFile
import java.time.Clock
import java.time.Instant
import java.time.ZoneId


class MarvelApiServiceAuthenticatorInterceptorTest {
    companion object {
        const val FAKE_MARVEL_PUBLIC_KEY = "a"
        const val FAKE_MARVEL_PRIVATE_KEY = "b"
    }

    private val fixedClock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault())
    private val mockWebServer = MockWebServer()
    private val fixedTimeStamp = Instant.now(fixedClock).epochSecond.toString()

    private lateinit var marvelApi: MarvelApiService
    private lateinit var marvelAuthenticatorInterceptor: MarvelApiServiceAuthenticatorInterceptor

    @Before
    fun setUp() {
        marvelAuthenticatorInterceptor = MarvelApiServiceAuthenticatorInterceptor(
            FAKE_MARVEL_PUBLIC_KEY,
            FAKE_MARVEL_PRIVATE_KEY,
            clock = fixedClock
        )

        marvelApi = CharactersModule.provideMarvelApiService(
            endPoint = mockWebServer.url("/").toString(),
            apiAuthenticator = marvelAuthenticatorInterceptor,
            clientBuilder = OkHttpClient.Builder()
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `when doing a request, the url appends an apikey parameter with the public api key`() {
        runBlocking {
            val successCharacterResponse = MockResponse.fromFile("Characters.json")
            mockWebServer.enqueue(successCharacterResponse)

            marvelApi.characters()

            val apiKey = mockWebServer.takeRequest().requestUrl?.queryParameter("apikey")
            assertThat(apiKey).isEqualTo(FAKE_MARVEL_PUBLIC_KEY)
        }
    }

    @Test
    fun `when doing a request, the url appends a ts parameter with a timestamp`() {
        runBlocking {
            val successCharacterResponse = MockResponse.fromFile("Characters.json")
            mockWebServer.enqueue(successCharacterResponse)

            marvelApi.characters()

            val apiKey = mockWebServer.takeRequest().requestUrl?.queryParameter("ts")
            assertThat(apiKey).isEqualTo(fixedTimeStamp)
        }
    }

    @Test
    fun `when doing a request, the url appends a hash parameter with a md5 = ts+privateKey+publicKey`() {
        runBlocking {
            val successCharacterResponse = MockResponse.fromFile("Characters.json")
            mockWebServer.enqueue(successCharacterResponse)

            marvelApi.characters()

            val hash = mockWebServer.takeRequest().requestUrl?.queryParameter("hash")
            val md5 = (fixedTimeStamp + FAKE_MARVEL_PRIVATE_KEY + FAKE_MARVEL_PUBLIC_KEY).md5()
            assertThat(md5).isEqualTo(hash)
        }
    }
}
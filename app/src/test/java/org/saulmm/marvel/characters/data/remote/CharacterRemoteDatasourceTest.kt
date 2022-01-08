package org.saulmm.marvel.characters.data.remote

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.saulmm.marvel.characters.data.remote.api.MarvelApiService
import org.saulmm.marvel.characters.data.remote.api.MarvelApiServiceAuthenticatorInterceptor
import org.saulmm.marvel.characters.di.CharactersModule
import org.saulmm.marvel.app.di.EndpointModule
import org.saulmm.marvel.app.utils.fromFileReplacingUrls

class CharacterRemoteDatasourceTest {
    private val mockWebServer = MockWebServer()
    private lateinit var marvelApi: MarvelApiService
    private val mockWebServerUrl = mockWebServer.url("/").toString()

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        marvelApi = CharactersModule.provideMarvelApiService(
            endPoint = mockWebServerUrl,
            apiAuthenticator = MarvelApiServiceAuthenticatorInterceptor("", ""),
            clientBuilder = OkHttpClient.Builder()
        )
    }

    private val character1016823RequestsDispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val url = checkNotNull(request.requestUrl?.toString())
            val serverUrl = mockWebServerUrl.removeSuffix("/")
            return when {
                url.contains("/v1/public/characters") ->
                    MockResponse.fromFileReplacingUrls(
                        fileName = "Character_1016823.json",
                        replaceFrom = EndpointModule.provideEndpoints(),
                        replaceTo = serverUrl
                    )

                url.contains("/v1/public/comics/40638") ->
                    MockResponse.fromFileReplacingUrls(
                        fileName = "Comic_1016823_40638.json",
                        replaceFrom = EndpointModule.provideEndpoints(),
                        replaceTo = serverUrl
                    )

                url.contains("/v1/public/comics/15717") ->
                    MockResponse.fromFileReplacingUrls(
                        fileName = "Comic_1016823_15717.json",
                        replaceFrom = EndpointModule.provideEndpoints(),
                        replaceTo = serverUrl
                    )

                else -> throw IllegalStateException("Unknown $url")
            }
        }
    }

    @Test
    fun `when getting a character detail this is retrieved with comics`() = runTest {
        mockWebServer.dispatcher = character1016823RequestsDispatcher

        val dataSource = CharacterRemoteDatasource(
            apiService = marvelApi,
            io = Dispatchers.Main
        )

        val characterDetail = dataSource.character(id = 1016823, comicsLimit = 4)

        assertThat(characterDetail?.comics?.size).isEqualTo(2)
    }

    @Test
    fun `when getting a character detail this is parsed correctly`() = runTest {
        mockWebServer.dispatcher = character1016823RequestsDispatcher

        val dataSource = CharacterRemoteDatasource(
            apiService = marvelApi,
            io = Dispatchers.Main
        )

        val characterDetail = dataSource.character(id = 1016823, comicsLimit = 4)

        assertThat(characterDetail?.name).isEqualTo("Abomination (Ultimate)")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }
}
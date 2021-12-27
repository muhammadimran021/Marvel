package org.saulmm.marvel.characters.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response
import org.saulmm.marvel.utils.ext.md5
import java.time.Clock
import java.time.Instant

class MarvelApiServiceAuthenticatorInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    private val clock: Clock = Clock.systemUTC()
): Interceptor {

    private companion object {
        // ts - a timestamp (or other long string which can change on a request-by-request basis)
        const val PARAM_TS = "ts"
        const val PARAM_API_KEY = "apikey"
        const val PARAM_HASH = "hash"
    }

    private val timeStamp: String
        get() = Instant.now(clock).epochSecond.toString()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val ts = timeStamp

        val hash = generateHash(
            publicKey = publicKey,
            privateKey = privateKey,
            timeStamp = ts
        )

        val newUrl = request.url.newBuilder()
            .addQueryParameter(PARAM_TS, ts)
            .addQueryParameter(PARAM_API_KEY, publicKey)
            .addQueryParameter(PARAM_HASH, hash)
            .build()

        val newRequest = request.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }

    /**
     * Generates a hash parameter, required by the marvel api to sign the requests.
     * [Authorizing and Signing Requests](https://developer.marvel.com/documentation/authorization)
     *
     * @param publicKey public key provided when creating an account.
     * @param privateKey private key provided when creating an account.
     * @param timeStamp a timestamp (or other long string which can change on a request-by-request basis)
     *
     * @return A md5 digest of the ts parameter, the private key, and the public key.
     * (e.g. md5(ts+privateKey+publicKey)
     */
    private fun generateHash(
        publicKey: String,
        privateKey: String,
        timeStamp: String
    ): String {
        return (timeStamp + privateKey + publicKey).md5()
    }
}
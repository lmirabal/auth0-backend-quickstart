package quickstart.oauth

import org.http4k.core.Body
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.format.Jackson.auto

class OAuthTokenFetcher(private val config: OAuthConfig, private val http: HttpHandler) {

    fun fetch(credentials: Credentials = envCredentials()): TokenResult {
        val tokenResponse = http(
            Request(Method.POST, config.tokenUrl)
                .with(
                    requestLens of TokenRequest(credentials.user, credentials.password, config.audience)
                )
        )
        return resultLens(tokenResponse)
    }

    data class TokenRequest(
        val client_id: String,
        val client_secret: String,
        val audience: String,
        val grant_type: String = "client_credentials"
    )

    data class TokenResult(val access_token: String, val token_type: String) {
        fun toBearerToken() = "Bearer $access_token"
    }

    companion object {
        val requestLens = Body.auto<TokenRequest>().toLens()
        val resultLens = Body.auto<TokenResult>().toLens()

        fun envCredentials() =
            Credentials(System.getenv("CLIENT_ID"), System.getenv("CLIENT_SECRET"))
    }
}
package quickstart

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import quickstart.oauth.OAuthConfig
import quickstart.oauth.OAuthTokenFetcher

class EndToEndTest {
    private val client = OkHttp()
    private val server = quickstartServer(0)

    @Test
    fun `responds to ping`() {
        assertThat(client(Request(GET, "http://localhost:${server.port()}/ping")), hasStatus(OK))
    }

    @Test
    fun `responds to public endpoint`() {
        val response = client(Request(GET, "http://localhost:${server.port()}/api/public"))

        assertThat(response, hasStatus(OK) and hasBody("Hello from a public endpoint!"))
    }

    @Test
    fun `unauthorised to use private endpoint without auth`() {
        val response = client(Request(GET, "http://localhost:${server.port()}/api/private"))

        assertThat(response, hasStatus(UNAUTHORIZED))
    }

    @Test
    fun `responds to private endpoint when valid bearer token included`() {
        val tokenFetcher = OAuthTokenFetcher(OAuthConfig("https://dev-u5v4iec0.eu.auth0.com", "https://quickstart/api"), client)

        val response = client(
            Request(GET, "http://localhost:${server.port()}/api/private")
                .header("authorization", tokenFetcher.fetch().toBearerToken())
        )

        assertThat(response, hasStatus(OK) and hasBody("Hello from a private endpoint!"))
    }

    @BeforeEach
    fun setup() {
        server.start()
    }

    @AfterEach
    fun tearDown() {
        server.stop()
    }
}
package quickstart

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.client.OkHttp
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Credentials
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.string
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import quickstart.oauth.OAuthTokenFetcher

class EndToEndTest {
    private val client = OkHttp()
    private val server = quickstartServer(testEnvironment)

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
        val tokenFetcher = OAuthTokenFetcher(oAuthConfig(testEnvironment), client)

        val response = client(
            Request(GET, "http://localhost:${server.port()}/api/private")
                .header("authorization", tokenFetcher.fetch(clientCredentials(testEnvironment)).toBearerToken())
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

val clientId = EnvironmentKey.string().required("client.id")
val clientSecret = EnvironmentKey.string().required("client.secret")
fun clientCredentials(environment: Environment) = Credentials(clientId(environment), clientSecret(environment))
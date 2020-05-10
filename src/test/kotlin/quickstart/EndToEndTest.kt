package quickstart

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EndToEndTest {
    private val client = OkHttp()
    private val server = quickstartServer(0)

    @BeforeEach
    fun setup() {
        server.start()
    }

    @AfterEach
    fun tearDown() {
        server.stop()
    }

    @Test
    fun `responds to ping`() {
        assertThat(client(Request(Method.GET, "http://localhost:${server.port()}/ping")), hasStatus(OK))
    }

    @Test
    fun `responds to public endpoint`() {
        val response = client(Request(Method.GET, "http://localhost:${server.port()}/api/public"))
        assertThat(response, hasStatus(OK) and hasBody("Hello from a public endpoint!"))
    }
}
package quickstart

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import quickstart.oauth.JwtVerifier
import quickstart.oauth.OAuthConfig

fun quickstartServer(port: Int): Http4kServer = quickstartApp().asServer(Jetty(port))

fun quickstartApp(): HttpHandler {
    return DebuggingFilters.PrintRequestAndResponse()
        .then(
            routes(
                "/ping" bind GET to { Response(OK) },
                "/api/public" bind GET to { Response(OK).body("Hello from a public endpoint!") },
                "/api/private" bind GET to ServerFilters.BearerAuth(
                    JwtVerifier(
                        OAuthConfig(
                            "https://dev-u5v4iec0.eu.auth0.com",
                            "https://quickstart/api"
                        )
                    )
                )
                    .then { Response(OK).body("Hello from a private endpoint!") }
            )
        )
}
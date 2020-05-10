package quickstart

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.lens.uri
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import quickstart.oauth.JwtVerifier
import quickstart.oauth.OAuthConfig

fun quickstartServer(environment: Environment): Http4kServer =
    quickstartApp(oAuthConfig(environment)).asServer(Jetty(serverPort(environment)))

fun quickstartApp(oAuthConfig: OAuthConfig): HttpHandler {
    return DebuggingFilters.PrintRequestAndResponse()
        .then(
            routes(
                "/ping" bind GET to { Response(OK) },
                "/api/public" bind GET to { Response(OK).body("Hello from a public endpoint!") },
                "/api/private" bind GET to ServerFilters.BearerAuth(JwtVerifier(oAuthConfig))
                    .then { Response(OK).body("Hello from a private endpoint!") }
            )
        )
}

val serverPort = EnvironmentKey.int().required("server.port")
val oAuthServerUrl = EnvironmentKey.uri().required("oauth.server.url")
val oAuthAudience = EnvironmentKey.string().required("oauth.audience")

fun oAuthConfig(environment: Environment) = OAuthConfig(oAuthServerUrl(environment), oAuthAudience(environment))

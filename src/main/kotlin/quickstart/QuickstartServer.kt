package quickstart

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.RequestContexts
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.lens.uri
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import quickstart.oauth.BearerToken
import quickstart.oauth.JwtDecoder
import quickstart.oauth.OAuthConfig
import quickstart.oauth.TokenScope

fun quickstartServer(environment: Environment): Http4kServer =
    quickstartApp(oAuthConfig(environment)).asServer(Jetty(serverPort(environment)))

fun quickstartApp(oAuthConfig: OAuthConfig): HttpHandler {
    val contexts = RequestContexts()
    val bearerToken = RequestContextKey.required<BearerToken>(contexts)
    return DebuggingFilters.PrintRequestAndResponse()
        .then(
            routes(
                "/ping" bind GET to { Response(OK) },
                "/api/public" bind GET to { Response(OK).body("Hello from a public endpoint!") },
                "/api/private" bind ServerFilters.InitialiseRequestContext(contexts)
                    .then(ServerFilters.BearerAuth(bearerToken, JwtDecoder(oAuthConfig)))
                    .then(
                        routes(
                            "/" bind GET to { Response(OK).body("Hello from a private endpoint!") },
                            "/read" bind GET to TokenScopeFilter(bearerToken, TokenScope("read:messages"))
                                .then { Response(OK).body("Hello from a read private endpoint!") }
                        )
                    )
            )
        )
}

val serverPort = EnvironmentKey.int().required("server.port")
val oAuthServerUrl = EnvironmentKey.uri().required("oauth.server.url")
val oAuthAudience = EnvironmentKey.string().required("oauth.audience")

fun oAuthConfig(environment: Environment) = OAuthConfig(oAuthServerUrl(environment), oAuthAudience(environment))

object TokenScopeFilter {
    operator fun invoke(bearerToken: RequestContextLens<BearerToken>, requiredScope: TokenScope) = Filter { next ->
        { request ->
            if (bearerToken(request).tokenScope == requiredScope) next(request)
            else Response(FORBIDDEN)
        }
    }
}
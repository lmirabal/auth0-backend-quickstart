package quickstart

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun quickstartServer(port: Int): Http4kServer = quickstartApp().asServer(Jetty(port))

fun quickstartApp(): HttpHandler =
    DebuggingFilters.PrintRequestAndResponse()
        .then(
            routes(
                "/ping" bind GET to { Response(OK) },
                "/api/public" bind GET to { Response(OK).body("Hello from a public endpoint!")}
            )
        )
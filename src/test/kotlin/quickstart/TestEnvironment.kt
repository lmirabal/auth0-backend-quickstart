package quickstart

import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri

val testEnvironment = Environment.ENV overrides Environment.defaults(
    serverPort of 0,
    oAuthServerUrl of Uri.of("https://dev-u5v4iec0.eu.auth0.com"),
    oAuthAudience of "https://quickstart/api"
)
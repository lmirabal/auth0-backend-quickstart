package quickstart.oauth

import org.http4k.core.Uri

data class OAuthConfig(val uri: Uri, val audience: String) {
    val jwkUrl = "$uri/.well-known/jwks.json"
    val tokenUrl = "$uri/oauth/token"
    val issuer = "$uri/"
}
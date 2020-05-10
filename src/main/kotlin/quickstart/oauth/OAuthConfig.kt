package quickstart.oauth

data class OAuthConfig(val uri: String, val audience: String) {
    val jwkUrl = "$uri/.well-known/jwks.json"
    val tokenUrl = "$uri/oauth/token"
    val issuer = "$uri/"
}
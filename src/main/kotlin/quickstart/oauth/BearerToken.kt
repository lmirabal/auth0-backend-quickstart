package quickstart.oauth

data class BearerToken(val tokenScope: TokenScope?)
data class TokenScope(val scope: String)
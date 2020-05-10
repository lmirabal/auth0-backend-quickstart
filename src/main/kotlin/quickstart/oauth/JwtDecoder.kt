package quickstart.oauth

import com.auth0.jwk.GuavaCachedJwkProvider
import com.auth0.jwk.JwkException
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.net.URI
import java.security.interfaces.RSAPublicKey

class JwtDecoder(private val oauthConfig: OAuthConfig) : (String) -> BearerToken? {
    private val jwkProvider: JwkProvider = GuavaCachedJwkProvider(
        UrlJwkProvider(URI.create(oauthConfig.jwkUrl).toURL())
    )

    override operator fun invoke(bearerToken: String): BearerToken? = try {
        val decodedJwt = JWT.decode(bearerToken)
        val publicKey = jwkProvider.get(decodedJwt.keyId).publicKey as RSAPublicKey
        val verifier = JWT.require(Algorithm.RSA256(publicKey, null))
            .withAudience(oauthConfig.audience)
            .withIssuer(oauthConfig.issuer)
            .build()
        verifier.verify(bearerToken)

        val scope: String? = decodedJwt.getClaim("scope").asString()
        BearerToken(scope?.let { TokenScope(it) })
    } catch (e: JWTVerificationException) {
        null
    } catch (e: JwkException) {
        null
    }
}
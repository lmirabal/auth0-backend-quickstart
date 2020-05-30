# Kotlin Auth0 backend quickstart 

This example shows how to secure an HTTP API implemented in Kotlin.

It is implemented using lightweight libraries to allow showing the underlying security concepts.
 
- HTTP: [Http4k](https://github.com/http4k/http4k/).
- JWT: [Auth0 JWT](https://github.com/auth0/java-jwt).
- JWKS: [Auth0 JWKS](https://github.com/auth0/jwks-rsa-java).

Examples using other technologies are available in [Auth0 quickstarts](https://auth0.com/docs/quickstart/backend).

### Pre-requisites

- OAuth2 Authorisation server: The configuration included in the sources use a free personal [Auth0](https://auth0.com/)
account or any other alternative could be configured setting environment variables: `OAUTH_SERVER_URL` and
`OAUTH.AUDIENCE`.

- Client credentials need to be configured in the authorisation server and then provide them via environment variables: 
    - Client with no scopes included in the token claims: `CLIENT_ID` and `CLIENT_SECRET`.
    - Client with "read:messages" scope: `READ_CLIENT_ID` and `READ_CLIENT_SECRET`.

### Tests

This example only includes end to end tests that runs the application running on a Jetty HTTP server.

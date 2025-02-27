package com.example.koboard.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.koboard.dto.TokenDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class JwtServiceImpl(
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.access.secret}") private val secret: String,
    @Value("\${jwt.access.expiration}") private val expiration: Long,
    @Value("\${jwt.refresh.secret}") private val refreshSecret: String,
    @Value("\${jwt.refresh.expiration}") private val refreshExpiration: Long
): JwtService {
    override fun createAccessToken(name:String): TokenDto {
        val currentTimeMillis = System.currentTimeMillis()
        val expiresAt = Date(currentTimeMillis + expiration)
        val token = JWT.create()
            .withClaim("username", name)
            .withClaim("role", "user")
            .withIssuer(issuer)
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC512(secret.toByteArray()))

        val localExpiresAt = expiresAt.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return TokenDto(token, localExpiresAt)
    }
    override fun jwtDecoder(jwt: String): String {
        val jwtVerifier = JWT
            .require(Algorithm.HMAC512(secret.toByteArray()))
            .withIssuer(issuer)
            .build()
        val verify = jwtVerifier.verify(jwt)

        return verify.claims["username"]?.asString() ?: ""
    }
    override fun createRefreshToken(jwt: String): TokenDto {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis + refreshExpiration)
        val jwtVerifier = JWT.create()
            .withClaim("username", jwt)
            .withIssuer(issuer)
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(date)
            .sign(Algorithm.HMAC512(refreshSecret.toByteArray()))
        val expiresAt = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return TokenDto(jwtVerifier, expiresAt)
    }
    override fun jwtRefreshDecoder(jwt: String): String {
        val jwtVerifier = JWT
            .require(Algorithm.HMAC512(refreshSecret.toByteArray()))
            .withIssuer(issuer)
            .build()
        val verify = jwtVerifier.verify(jwt)

        return verify.claims["username"]?.asString() ?: ""
    }


}
package com.example.koboard.config

import com.auth0.jwt.exceptions.TokenExpiredException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class JwtServiceImplTest {
    private lateinit var jwtServiceImpl: JwtServiceImpl
    @BeforeEach
    fun setUp() {
        jwtServiceImpl = JwtServiceImpl(
                "hi",
        "hifdsafsdafdasfdsa",
        60000,
        "hifdsafsdafdasfdsawww",
        3600000
        )
    }
    @Test
    fun createAccessToken() {

        val jwtEncoder = jwtServiceImpl.createAccessToken("hi")
        assertEquals(3, jwtEncoder.token.split(".").size)


    }

    @Test
    fun `jwtDecoder-TimeOut`() {
        val assertThrows =
            assertThrows<TokenExpiredException> { jwtServiceImpl.jwtDecoder("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImhpIiwicm9sZSI6InVzZXIiLCJpc3MiOiJoaSIsImlhdCI6MTczMTkxNjY2MywiZXhwIjoxNzMxOTE2NzIzfQ.PFnoQcGJ463W6raQtHDuocBgWsJ1P-0f8C6_DiRjp4V8o-puhgxdwtz2n84eNiog02lO26gZm0WVAqlP1FnNfQ") }


    }
    @Test
    fun jwtDecoder2() {
        val jwtDecoder = jwtServiceImpl.jwtDecoder(jwtServiceImpl.createAccessToken("hi").token)
        assertEquals("hi", jwtDecoder)
    }
}
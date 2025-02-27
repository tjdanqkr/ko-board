package com.example.koboard.study

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class IntTest {
    @Test
    fun minusTest(){
       // given
        var a = 10
        // when
        a.minus(4)
        // then
        assertEquals(10, a)
    }
}
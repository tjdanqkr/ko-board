package com.example.koboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KoBoardApplication

fun main(args: Array<String>) {
    runApplication<KoBoardApplication>(*args)
}


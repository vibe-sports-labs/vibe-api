package com.vibe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VibeApiApplication

fun main(args: Array<String>) {
	runApplication<VibeApiApplication>(*args)
}

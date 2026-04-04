package com.vibe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class VibeApiApplication

fun main(args: Array<String>) {
	runApplication<VibeApiApplication>(*args)
}

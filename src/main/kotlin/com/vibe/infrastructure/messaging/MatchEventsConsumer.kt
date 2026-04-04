package com.vibe.infrastructure.messaging

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class MatchEventConsumer {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["match.events"])
    fun handleMatchJoined(payload: String) {
        logger.info("🔔 NOTIFICAÇÃO RECEBIDA: $payload")
        logger.info("🚀 Enviando alerta para o celular do organizador...")
    }
}
package com.vibe.infrastructure.messaging

import com.vibe.infrastructure.configuration.RabbitMQConfig.Companion.MATCH_EVENTS_EXCHANGE
import com.vibe.infrastructure.repository.OutboxRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class OutboxRelay(
    private val outboxRepository: OutboxRepository,
    private val rabbitTemplate: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 5000)
    fun processOutbox() {
        val pendingEvents = outboxRepository.findByProcessedFalse()

        if (pendingEvents.isNotEmpty()) {
            logger.info("Encontrados ${pendingEvents.size} eventos para processar.")
        }

        pendingEvents.forEach { event ->
            try {
                rabbitTemplate.convertAndSend(MATCH_EVENTS_EXCHANGE, event.type.name, event.payload)

                event.processed = true
                outboxRepository.save(event)

                logger.info("✅ Evento ${event.type} enviado para o organizador!")
            } catch (e: Exception) {
                logger.error("❌ Erro ao enviar evento: ${e.message}. Tentará novamente em 5s.")
            }
        }
    }
}

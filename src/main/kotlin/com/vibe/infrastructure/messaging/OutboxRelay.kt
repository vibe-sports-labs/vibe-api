package com.vibe.infrastructure.messaging

import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.vibe.domain.OutboxEntry
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest
import org.springframework.data.mongodb.core.messaging.MessageListener
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer
import jakarta.annotation.PostConstruct
import org.bson.Document

@Configuration
class OutboxRelay(
    private val container: MessageListenerContainer,
    private val rabbitTemplate: RabbitTemplate
) {

    @PostConstruct
    fun startListening() {
        val request = ChangeStreamRequest.builder(MessageListener<ChangeStreamDocument<Document>, OutboxEntry> { message ->
            val outbox = message.body

            if (outbox != null) {
                try {
                    rabbitTemplate.convertAndSend(
                        "vibe.exchange",
                        "vibe.event.${outbox.type}",
                        outbox.payload
                    )
                    println("🚀 [Vibe-Relay] Evento enviado: ${outbox.type} | ID: ${outbox.aggregateId}")
                } catch (e: Exception) {
                    println("❌ [Vibe-Relay] Erro ao enviar para RabbitMQ: ${e.message}")
                }
            }
        })
            .collection("outbox")
            .build()

        container.register(request, OutboxEntry::class.java)
    }
}

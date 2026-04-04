package com.vibe.application.service

import com.vibe.domain.OutboxEntry
import com.vibe.domain.enums.OutboxEventType
import com.vibe.infrastructure.repository.OutboxRepository
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper

@Service
class OutboxService(
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper
) {

    fun <T : Any> saveEvent(aggregateId: String?, type: OutboxEventType, payload: T) {
        if (aggregateId == null) {
            throw IllegalArgumentException("aggregateId não pode ser nulo.")
        }

        val jsonPayload = objectMapper.writeValueAsString(payload)
        val entry = OutboxEntry(
            aggregateId = aggregateId,
            type = type,
            payload = jsonPayload
        )
        outboxRepository.save(entry)
    }

}
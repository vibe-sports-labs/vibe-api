package com.vibe.domain

import com.vibe.domain.enums.OutboxEventType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "outbox")
data class OutboxEntry (
    @Id val id: String? = null,
    val aggregateId: String,
    val type: OutboxEventType,
    val payload: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var processed: Boolean = false
)
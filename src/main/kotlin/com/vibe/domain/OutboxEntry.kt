package com.vibe.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "outbox")
data class OutboxEntry (
    @Id val id: String? = null,
    val aggregateId: String,
    val type: String,
    val payload: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var processed: Boolean = false
)
package com.vibe.infrastructure.repository

import com.vibe.domain.OutboxEntry
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OutboxRepository: MongoRepository<OutboxEntry, String> {
}
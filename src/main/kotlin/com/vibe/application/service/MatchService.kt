package com.vibe.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.vibe.domain.Match
import com.vibe.domain.OutboxEntry
import com.vibe.infrastructure.repository.MatchRepository
import com.vibe.infrastructure.repository.OutboxRepository
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchService(
    private val matchRepository: MatchRepository,
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun createMatch(match: Match): Match {
        val savedMatch = matchRepository.save(match)

        val matchCreatedEvent = mapOf(
            "matchId" to savedMatch.id,
            "organizerId" to savedMatch.organizerId,
            "title" to savedMatch.title
        )

        val outboxEntry = OutboxEntry(
            aggregateId = savedMatch.id ?: "",
            type = "MATCH_CREATED",
            payload = objectMapper.writeValueAsString(matchCreatedEvent)
        )

        outboxRepository.save(outboxEntry)

        return savedMatch
    }

    fun searchMatches(location: GeoJsonPoint, distanceMeters: Double, sport: String?): List<Match> {
        return if (!sport.isNullOrBlank()) {
            matchRepository.findByLocationAndSport(location, distanceMeters, sport)
        } else {
            matchRepository.findByLocationNear(location, distanceMeters)
        }
    }
}
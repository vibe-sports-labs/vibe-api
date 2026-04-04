package com.vibe.application.service

import com.vibe.domain.Match
import com.vibe.domain.OutboxEntry
import com.vibe.infrastructure.repository.MatchRepository
import com.vibe.infrastructure.repository.OutboxRepository
import com.vibe.interfaces.v1.dto.CreateMatchRequest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Service
class MatchService(
    private val matchRepository: MatchRepository
) {
    @Transactional
    fun createMatch(request: CreateMatchRequest, organizerId: String): Match {
        val geoPoint = GeoJsonPoint(request.longitude, request.latitude)

        val match = Match(
            title = request.title,
            organizerId = organizerId,
            location = geoPoint,
            addressName = request.addressName,
            startDateTime = request.startDateTime,
            maxPlayers = request.maxPlayers,
            entryFee = request.entryFee ?: java.math.BigDecimal.ZERO,
            sportId = request.sportId,
            endSubscriptionDate = request.endSubscriptionDate,
            currentPlayers = mutableListOf(organizerId)
        )

        return matchRepository.save(match)
    }

    fun searchMatches(location: GeoJsonPoint, distanceMeters: Double, sport: String?): List<Match> {
        return if (!sport.isNullOrBlank()) {
            matchRepository.findByLocationAndSport(location, distanceMeters, sport)
        } else {
            matchRepository.findByLocationNear(location, distanceMeters)
        }
    }
}
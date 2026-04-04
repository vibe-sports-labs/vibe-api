package com.vibe.application.service

import com.vibe.domain.Match
import com.vibe.domain.enums.MatchStatus
import com.vibe.domain.enums.OutboxEventType
import com.vibe.infrastructure.repository.MatchRepository
import com.vibe.interfaces.v1.dto.CreateMatchRequest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchService(
    private val matchRepository: MatchRepository,
    private val outboxService: OutboxService
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

    @Transactional
    fun joinMatch(matchId: String, userId: String): Match {
        val match = matchRepository.findById(matchId)
            .orElseThrow { NoSuchElementException("Partida não encontrada.") }

        if (match.currentPlayers.size >= match.maxPlayers) {
            throw IllegalStateException("A partida já está lotada.")
        }

        if (match.currentPlayers.contains(userId)) {
            throw IllegalStateException("Você já está inscrito nesta partida.")
        }

        match.currentPlayers.add(userId)

        if (match.currentPlayers.size == match.maxPlayers) {
            match.status = MatchStatus.FULL
        }

        val savedMatch = matchRepository.save(match)

        outboxService.saveEvent(savedMatch.id, OutboxEventType.MATCH_JOIN, savedMatch)

        return savedMatch
    }
}
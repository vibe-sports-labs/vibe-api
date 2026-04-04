package com.vibe.interfaces.v1

import com.vibe.application.service.MatchService
import com.vibe.domain.Match
import com.vibe.interfaces.v1.dto.CreateMatchRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "Gerenciamento de partidas e geolocalização")
@CrossOrigin(origins = ["*"])
class MatchControllerV1(private val matchService: MatchService) {

    @PostMapping
    @Operation(summary = "Cria uma nova partida", description = "Salva a partida no MongoDB e gera um evento de Outbox.")
    fun createMatch(@Valid @RequestBody request: CreateMatchRequest): ResponseEntity<Match> {
        val createdMatch = matchService.createMatch(request, "organizerId")
        return ResponseEntity.ok(createdMatch)
    }

    @GetMapping("nearby")
    @Operation(summary = "Busca partidas próximas", description = "Filtra por coordenadas (longitude/latitude) e opcionalmente por nome do esporte.")
    fun getNearbyMatches(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam(required = false) sport: String?,
        @RequestParam(defaultValue = "5000") distanceMeters: Double
    ): ResponseEntity<List<Match>> {
        val userLocation = GeoJsonPoint(longitude, latitude)
        val matches = matchService.searchMatches(userLocation, distanceMeters, sport)
        return ResponseEntity.ok(matches)
    }

    @PatchMapping("/{id}/join")
    fun joinMatch(@PathVariable id: String): ResponseEntity<Any> {
        // Simulando o usuário logado (depois virá do SecurityContext)
        val currentUserId = "user_icaro_789"

        return try {
            val updatedMatch = matchService.joinMatch(id, currentUserId)
            ResponseEntity.ok(updatedMatch)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }
}
package com.vibe.interfaces.v1

import com.vibe.application.service.MatchService
import com.vibe.domain.Match
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "Gerenciamento de partidas e geolocalização")
@CrossOrigin(origins = ["*"])
class MatchControllerV1(private val matchService: MatchService) {

    @PostMapping
    @Operation(summary = "Cria uma nova partida", description = "Salva a partida no MongoDB e gera um evento de Outbox.")
    fun createMatch(@RequestBody match: Match): ResponseEntity<Match> {
        val createdMatch = matchService.createMatch(match)
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
}
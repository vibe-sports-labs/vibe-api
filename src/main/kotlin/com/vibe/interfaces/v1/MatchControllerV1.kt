package com.vibe.interfaces.v1

import com.vibe.application.service.MatchService
import com.vibe.domain.Match
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/matches")
class MatchControllerV1(private val matchService: MatchService) {

    @PostMapping
    fun createMatch(@RequestBody match: Match): ResponseEntity<Match> {
        val createdMatch = matchService.createMatch(match)
        return ResponseEntity.ok(createdMatch)
    }

    @GetMapping("nearby")
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
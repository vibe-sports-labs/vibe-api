package com.vibe.infrastructure.repository

import com.vibe.domain.Match
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository: MongoRepository<Match, String> {
    fun findByLocationNear(point: GeoJsonPoint, maxDistance: Double): List<Match>

    @Query($$"{ 'location': { $near: { $geometry: ?0, $maxDistance: ?1 } }, 'sportName': { $regex: ?2, $options: 'i' } }")
    fun findByLocationAndSport(point: GeoJsonPoint, maxDistance: Double, sportName: String): List<Match>
}
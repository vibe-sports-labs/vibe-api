package com.vibe.domain

import com.vibe.domain.enums.Status
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "matches")
data class Match(
    @Id
    val id: String? = null,
    val title: String,
    val organizerId: String,
    val sport: Sport,
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    val location: GeoJsonPoint,
    val addressName: String,
    val startDateTime: LocalDateTime,
    val endSubscriptionDate: LocalDateTime,
    val maxPlayers: Int,
    val currentPlayers: MutableList<String> = mutableListOf(),
    val entryFee: BigDecimal? = null,
    val status: Status = Status.OPEN
)
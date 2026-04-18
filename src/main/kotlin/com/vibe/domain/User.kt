package com.vibe.domain

import com.vibe.domain.enums.Roles
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "users")
data class User(
    @Id val id: String? = null,
    val firebaseUid: String,
    val email: String,
    val name: String? = null,
    val photoUrl: String? = null,
    val roles: List<Roles> = listOf(Roles.PLAYER),
    val phone: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastLogin: LocalDateTime = LocalDateTime.now()
)

package com.vibe.infrastructure.repository

import com.vibe.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, String> {
    fun findByFirebaseUid(firebaseUid: String): User?
}
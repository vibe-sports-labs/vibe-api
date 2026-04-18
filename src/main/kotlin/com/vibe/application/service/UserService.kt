package com.vibe.application.service

import com.vibe.domain.User
import com.vibe.infrastructure.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun syncUser(firebaseUid: String, email: String, name: String? = null, photoUrl: String? = null): User {
        val existingUser = userRepository.findByFirebaseUid(firebaseUid)

        if (existingUser == null) {
            val newUser = User(firebaseUid = firebaseUid, email = email, name = name, photoUrl = photoUrl)
            logger.info("🆕 Novo atleta registrado (UID: $firebaseUid, Email: $email)")
            return userRepository.save(newUser)
        }

        return existingUser
    }
}
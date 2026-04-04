package com.vibe.infrastructure.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfig {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun firebaseApp(): FirebaseApp? {
        return try {
            val serviceAccount = ClassPathResource("service-account.json").inputStream

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            } else {
                FirebaseApp.getInstance()
            }
        } catch (e: Exception) {
            logger.error("Erro ao inicializar o Firebase: ${e.message}")
            null
        }
    }
}
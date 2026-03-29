package com.vibe.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer

@Configuration
class MongoConfig {

    @Bean
    fun messageListenerContainer(mongoTemplate: MongoTemplate): MessageListenerContainer {
        return DefaultMessageListenerContainer(mongoTemplate)
    }
}
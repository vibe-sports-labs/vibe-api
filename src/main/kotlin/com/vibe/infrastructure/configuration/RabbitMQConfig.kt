package com.vibe.infrastructure.configuration

import com.vibe.domain.enums.OutboxEventType
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    companion object {
        const val MATCH_EVENTS_EXCHANGE = "match.events.exchange"
        const val MATCH_EVENTS_QUEUE = "match.events.queue"
    }

    @Bean
    fun matchEventsQueue() = Queue(MATCH_EVENTS_QUEUE, true)

    @Bean
    fun matchEventsExchange() = DirectExchange(MATCH_EVENTS_EXCHANGE)

    @Bean
    fun binding(queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(OutboxEventType.MATCH_JOIN.name)
    }
}
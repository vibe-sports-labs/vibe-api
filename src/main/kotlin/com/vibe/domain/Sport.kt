package com.vibe.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sports")
data class Sport (
    @Id
    val id: String? = null,
    val name: String,
    val icon: String? = null
)
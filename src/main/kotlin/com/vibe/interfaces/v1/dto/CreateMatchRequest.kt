package com.vibe.interfaces.v1.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateMatchRequest(
    @NotNull(message = "O título da partida é obrigatório.")
    @NotBlank(message = "O título da partida não pode estar vazio.")
    val title: String,
    @NotBlank(message = "O tipo de esporte é obrigatório.")
    val sportId: String,
    @NotNull(message = "A latitude é obrigatória.")
    @Min(value = -90, message = "Latitude inválida.")
    @Max(value = 90, message = "Latitude inválida.")
    val latitude: Double,
    @NotNull(message = "A longitude é obrigatória.")
    @Min(value = -180, message = "Longitude inválida.")
    @Max(value = 180, message = "Longitude inválida.")
    val longitude: Double,
    @NotBlank(message = "O endereço é obrigatório.")
    val addressName: String,
    @Future(message = "A partida deve ser marcada para uma data futura.")
    val startDateTime: LocalDateTime,
    @Future(message = "A data de inscrição deve ser uma data futura.")
    val endSubscriptionDate: LocalDateTime,
    @NotNull(message = "O número máximo de jogadores é obrigatório.")
    @Min(value = 2, message = "O número mínimo de jogadores é 2.")
    val maxPlayers: Int,
    val entryFee: BigDecimal?
)

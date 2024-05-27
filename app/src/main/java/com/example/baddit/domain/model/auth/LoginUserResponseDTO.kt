package com.example.baddit.domain.model.auth

data class LoginUserResponseDTO(
    val avatarUrl: String,
    val email: String,
    val emailVerified: Boolean,
    val id: String,
    val registeredAt: String,
    val role: String,
    val username: String
)
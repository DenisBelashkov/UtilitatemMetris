package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class SuccessfulLoginUser(
        val email: String,
        val id: Int,
        val token: String
)
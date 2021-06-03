package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class SuccessfulLoginUser(
        var email: String = "",
        var id: Int = 0,
        var token: String = ""
)
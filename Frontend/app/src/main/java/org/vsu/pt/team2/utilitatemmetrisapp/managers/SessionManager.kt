package org.vsu.pt.team2.utilitatemmetrisapp.managers

object SessionManager {
    var email: String = "email.email@email"
    var id: Int = 123
    var isDemo: Boolean = false
    var jwt: String = "empty"

    fun setSession(
        id: Int,
        email: String,
        isDemo: Boolean,
        jwt: String
    ) {
        this.email = email
        this.id = id
        this.isDemo = isDemo
        this.jwt = jwt
    }
}
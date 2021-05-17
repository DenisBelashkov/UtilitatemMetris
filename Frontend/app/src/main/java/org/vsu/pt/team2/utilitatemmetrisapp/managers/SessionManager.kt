package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.User

object SessionManager {

    private final val EmptyUser = User(0, "", "")

    var user: User = this.EmptyUser

    var isDemo: Boolean = false

    fun setSession(
            user: User,
            isDemo: Boolean
    ) {
        this.user = user
        this.isDemo = isDemo
    }

    fun clear() {
        this.user = EmptyUser
        this.isDemo = true
    }

    fun isAuthorized() = user != EmptyUser


}
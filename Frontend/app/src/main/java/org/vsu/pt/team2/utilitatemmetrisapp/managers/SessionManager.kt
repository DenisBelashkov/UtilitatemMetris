package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.User
import javax.inject.Inject

class SessionManager @Inject constructor(
    //todo shared prefs
) {

    private final val EmptyUser = User(0, "", "")

    var user: User = this.EmptyUser
        get
        private set

    var isDemo: Boolean = false
        get
        private set

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
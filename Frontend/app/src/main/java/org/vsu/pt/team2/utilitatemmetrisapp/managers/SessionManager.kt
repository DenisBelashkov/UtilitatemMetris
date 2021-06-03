package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.User
import org.vsu.pt.team2.utilitatemmetrisapp.storage.Storage
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val storage: Storage
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
        storage.saveUser(user, isDemo)
    }

    fun clear() {
        this.user = EmptyUser
        this.isDemo = true
        storage.clearUser()
    }

    fun loadPreviousSessionOrUnauth(): Boolean {
        storage.getUser()?.let {
            this.user = it.first
            this.isDemo = it.second
            return true
        }
        this.user = EmptyUser
        this.isDemo = true
        return false
    }

    fun isAuthorized() = user != EmptyUser


}
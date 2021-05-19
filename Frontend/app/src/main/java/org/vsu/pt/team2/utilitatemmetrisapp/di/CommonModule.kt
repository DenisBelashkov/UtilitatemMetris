package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Module
import dagger.Provides
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import javax.inject.Singleton

@Module
class CommonModule {

    @Provides
    @Singleton
    fun sessionManager(): SessionManager {
        return SessionManager()
    }
}

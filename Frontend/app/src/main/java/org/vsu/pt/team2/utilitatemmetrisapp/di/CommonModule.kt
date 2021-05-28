package org.vsu.pt.team2.utilitatemmetrisapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import org.vsu.pt.team2.utilitatemmetrisapp.storage.Storage
import javax.inject.Singleton


@Module
class CommonModule {

    @Provides
    @Singleton
    fun providePreferences(
        application: Application
    ): SharedPreferences {
        return application.getSharedPreferences(
            "UMStore", Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideStorage(
        sharedPreferences: SharedPreferences
    ): Storage {
        return Storage(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        storage: Storage
    ): SessionManager {
        return SessionManager(storage)
    }
}

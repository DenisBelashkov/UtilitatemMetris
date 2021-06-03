package org.vsu.pt.team2.utilitatemmetrisapp.di

import android.app.Application
import android.content.SharedPreferences
import dagger.BindsInstance
import dagger.Component
import org.vsu.pt.team2.utilitatemmetrisapp.di.components.*
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import javax.inject.Singleton


@Component(modules = [NetworkModule::class, RepositoryModule::class, CommonModule::class])
@Singleton
interface ApplicationComponent {

    fun meterComponent(): MeterComponent

    fun accountComponent(): AccountComponent

    fun settingsComponent(): SettingsComponent

    fun authComponent(): AuthComponent

    fun paymentComponent(): PaymentComponent

    fun getSessionManager(): SessionManager

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}
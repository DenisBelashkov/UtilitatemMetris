package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Component
import org.vsu.pt.team2.utilitatemmetrisapp.network.BaseWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.SavedMetersFragment
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, RepositoryModule::class, ManagersModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(fragment: SavedMetersFragment)

    fun getMeterRepository(): MeterRepo

    fun getAccountRepository(): AccountRepo

    fun getBaseWorker(): BaseWorker
}
package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Module
import dagger.Provides
import org.vsu.pt.team2.utilitatemmetrisapp.managers.AccountManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.network.BaseWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject
import javax.inject.Singleton

@Module
class ManagersModule @Inject constructor(
) {

    @Provides
    @Singleton
    fun meterManager(
        baseWorker: BaseWorker,
        meterRepo: MeterRepo
    ): MeterManager = MeterManager(baseWorker, meterRepo)

    @Provides
    @Singleton
    fun accountManager(
        baseWorker: BaseWorker,
        accountRepo: AccountRepo
    ): AccountManager = AccountManager(baseWorker, accountRepo)
}
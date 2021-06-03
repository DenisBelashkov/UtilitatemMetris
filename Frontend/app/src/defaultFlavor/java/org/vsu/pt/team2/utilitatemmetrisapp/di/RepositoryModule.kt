package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.PaymentRepo
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAccountRepo(): AccountRepo = AccountRepo()

    @Provides
    @Singleton
    fun provideMeterRepo(): MeterRepo = MeterRepo()


    @Provides
    @Singleton
    fun providePaymentRepo(): PaymentRepo = PaymentRepo()
}
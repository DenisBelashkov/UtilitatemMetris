package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAccountRepo(): AccountRepo = AccountRepo()

    @Provides
    @Singleton
    fun provideMeterRepo(): MeterRepo = MeterRepo().apply {
        CoroutineScope(Dispatchers.IO).launch {
            addMeter(
                Meter(
                    "7a6d87asd",
                    MeterType.ColdWater,
                    3.5,
                    1234.0,
                    1273.0,
                    452.4
                )
            )
            addMeter(
                Meter(
                    "6633pqff445",
                    MeterType.Elect,
                    12.2,
                    433.0,
                    490.0,
                    1209.0
                )
            )
        }
    }
}
package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.SavedMeterRepo
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAccountRepo(): AccountRepo = AccountRepo()

    @Provides
    @Singleton
    fun provideMeterRepo(): SavedMeterRepo = SavedMeterRepo().apply {
        CoroutineScope(Dispatchers.IO).launch {
            addMeter(
                Meter(
                    "7a6d87asd",
                    MeterType.ColdWater,
                    3.5,
                    1234.0,
                    1273.0,
                    -452.4,
                    true
                )
            )
            addMeter(
                Meter(
                    "6633pqff445",
                    MeterType.Elect,
                    12.2,
                    433.0,
                    490.0,
                    -1209.0,
                    true
                )
            )
            addMeter(
                Meter(
                    "hh66h56h565",
                    MeterType.Elect,
                    55.6,
                    15.0,
                    18.0,
                    0.0,
                    true
                )
            )
        }
    }
}
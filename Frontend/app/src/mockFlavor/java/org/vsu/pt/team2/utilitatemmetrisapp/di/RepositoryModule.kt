package org.vsu.pt.team2.utilitatemmetrisapp.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.models.*
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.PaymentRepo
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAccountRepo(): AccountRepo = AccountRepo().apply {
        CoroutineScope(Dispatchers.IO).launch {
            addAccount(
                Account(
                    "123455ВА8В923",
                    "Воронеж, пр. революции, д. 1, кв 101"
                )
            )
            addAccount(
                Account(
                    "043534АВ3423А",
                    "Воронеж, ул. Пупкина, д. 13, кв 56"
                )
            )
        }
    }

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
                    -452.4,
                    "Ул. Ленина, д.123"
                ), true
            )
            addMeter(
                Meter(
                    "6633pqff445",
                    MeterType.Elect,
                    12.2,
                    433.0,
                    490.0,
                    -1209.0,
                    "Проспект Ленинский, д.12"
                ), true
            )
            addMeter(
                Meter(
                    "30053hh3vrc1",
                    MeterType.Gas,
                    1.2,
                    823.0,
                    823.0,
                    0.0,
                    "Проспект Ленинский, д.12"
                ), true
            )
        }
    }

    @Provides
    @Singleton
    fun providePaymentRepo(): PaymentRepo = PaymentRepo().apply {
        CoroutineScope(Dispatchers.IO).launch {
            addPayment(
                PaymentData(
                    1,
                    listOf(
                        PaymentMetricData(
                            Meter(
                                "12312j3h1jh2",
                                MeterType.Elect,
                                3.4,
                                1232.5,
                                1382.3,
                                0.0,
                                "Ул.Ленина д.123"
                            ),
                            892.2,
                            934.8,
                            150.0
                        ),
                        PaymentMetricData(
                            Meter(
                                "66j65ghvfc2",
                                MeterType.Gas,
                                1.3,
                                152.5,
                                183.3,
                                0.0,
                                "Ул.Ленина д.123"
                            ),
                            56.2,
                            62.8,
                            50.0
                        )
                    ),
                    "09.06.20 12:05:11",
                    "email@email.email"
                )
            )
            addPayment(
                PaymentData(
                    2,
                    listOf(
                        PaymentMetricData(
                            Meter(
                                "98765gfr2f",
                                MeterType.Gas,
                                1.3,
                                342.5,
                                389.3,
                                0.0,
                                "Пр-кт Ленинский д.23"
                            ),
                            270.2,
                            299.8,
                            50.0
                        )
                    ),
                    "08.01.20 18:23:00",
                    "email@email.email"
                )
            )
        }
    }
}
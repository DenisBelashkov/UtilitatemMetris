package org.vsu.pt.team2.utilitatemmetrisapp

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.vsu.pt.team2.utilitatemmetrisapp.di.DaggerApplicationComponent


class MyApplication : Application() {
    val appComponent = DaggerApplicationComponent.builder()
        .application(this)
        .build();

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())

        // Creating an extended library configuration.
        val config = YandexMetricaConfig.newConfigBuilder(API_key).build()
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this)
    }
}
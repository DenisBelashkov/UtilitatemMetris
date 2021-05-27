package org.vsu.pt.team2.utilitatemmetrisapp

import android.app.Application
import org.vsu.pt.team2.utilitatemmetrisapp.di.DaggerApplicationComponent

class MyApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()
}
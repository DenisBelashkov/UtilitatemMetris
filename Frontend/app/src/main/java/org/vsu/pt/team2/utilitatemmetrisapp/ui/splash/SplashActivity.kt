package org.vsu.pt.team2.utilitatemmetrisapp.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.yandex.metrica.YandexMetrica
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ActivitySplashBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.LoadingAnimationController
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.LoginActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MainActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.openActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var animControl: LoadingAnimationController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        YandexMetrica.reportEvent("Вход в приложение", null as String?);

        val binding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        animControl = LoadingAnimationController(applicationContext, binding.splashLoader)
    }

    override fun onStart() {
        super.onStart()
        startAnimation()
        lifecycleScope.launch {
            if (userAlreadyLoggedIn()) {
                YandexMetrica.reportEvent("Запуск главного экрана", null as String?);
                replaceActivity(MainActivity::class.java)
            }
            else {
                delay(1400L)
                YandexMetrica.reportEvent("Запуск экрана авторизации", null as String?);
                replaceActivity(LoginActivity::class.java)
            }
            clearAnimation()
        }
    }

    private fun userAlreadyLoggedIn(): Boolean {
        myApplication()?.appComponent?.getSessionManager()?.let {
            it.loadPreviousSessionOrUnauth()
            return it.isAuthorized()
        }
        return false
    }

    private fun startAnimation() {
        animControl.startAnimation()
    }

    private fun clearAnimation() {
        animControl.clearAnimation()
    }
}
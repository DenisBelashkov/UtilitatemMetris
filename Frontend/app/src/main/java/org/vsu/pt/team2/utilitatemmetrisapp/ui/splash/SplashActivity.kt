package org.vsu.pt.team2.utilitatemmetrisapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ActivitySplashBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.LoadingAnimationController
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var animControl: LoadingAnimationController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        animControl = LoadingAnimationController(applicationContext, binding.splashLoader)

        startAnimation()
        lifecycleScope.launch {
            delay(1400L)
            clearAnimation()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun startAnimation() {
        animControl.startAnimation()
    }

    private fun clearAnimation() {
        animControl.clearAnimation()
    }
}
package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import org.vsu.pt.team2.utilitatemmetrisapp.R

class LoadingAnimationController(
    context: Context,
    val iv: ImageView
) {
    val loadingAnimation = AnimationUtils.loadAnimation(
        context,
        R.anim.rotate_center
    ).also {
        it.interpolator = LinearInterpolator()
    }

    fun startAnimation(){
        iv.visibility = View.VISIBLE
        iv.startAnimation(loadingAnimation)
    }

    fun clearAnimation(){
        iv.visibility = View.GONE
        iv.clearAnimation()
    }
}
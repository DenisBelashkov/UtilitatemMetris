package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.BigGeneralButtonBinding

class BigGeneralButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defStyleRes) {
    var button: Button
    var loading_iv: ImageView
    var buttonText = ""
        set(value) {
            field = value
            button.text = value
        }
    var loadingAnimationController: LoadingAnimationController

    init {
        val binding = BigGeneralButtonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        loading_iv = binding.bigGeneralButtonImageView
        button = binding.bigGeneralButtonButton
        loadingAnimationController = LoadingAnimationController(
            context,
            loading_iv
        )

        attributeSet?.let {
            initComponents(
                context.obtainStyledAttributes(
                    it,
                    R.styleable.BigGeneralButton,
                    0,
                    R.style.BigGeneralButton
                )
            )
        }

        setStateDefault()
    }

    private fun initComponents(typedArray: TypedArray) {
        val textBtn = resources.getText(
            typedArray.getResourceId(
                R.styleable.BigGeneralButton_button_text,
                R.string.big_button_default_text
            )
        )
        val img_src = resources.getDrawable(
            typedArray.getResourceId(
                R.styleable.BigGeneralButton_src_image_loader,
                R.drawable.loading
            ),
//            resources.newTheme()
        )
        buttonText = textBtn.toString()
//        button.text = buttonText
        loading_iv.setImageDrawable(img_src)

        typedArray.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }

    override fun hasOnClickListeners(): Boolean {
        return button.hasOnClickListeners()
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        button.setOnLongClickListener(l)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return if (button.isActivated) button.performClick() else false
    }

    fun setStateLoading() {
        button.isActivated = false
        button.isEnabled = false
        button.visibility = View.VISIBLE
        button.text = ""

        loadingAnimationController.startAnimation()
    }

    fun setStateDefault() {
        button.isEnabled = true
        button.isActivated = true
        button.visibility = View.VISIBLE
        button.text = buttonText

        loadingAnimationController.clearAnimation()
    }
}

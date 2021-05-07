package org.vsu.pt.team2.utilitatemmetrisapp.ui.tools

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboardFrom(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun rusRubleAddition(num: Int): String {
    val preLastDigit = num % 100 / 10
    return if (preLastDigit == 1) {
        "рублей"
    } else when (num % 10) {
        1 -> "рубль"
        2, 3, 4 -> "рубля"
        else -> "рублей"
    }
}
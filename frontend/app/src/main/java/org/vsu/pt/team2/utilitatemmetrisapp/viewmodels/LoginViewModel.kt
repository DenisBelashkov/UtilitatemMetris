package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var isInLoginMode: Boolean = false

    val buttonText: String
        get() = if (isInLoginMode) "Войти" else "Зарегистрироваться"
}
package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val inLoginMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}
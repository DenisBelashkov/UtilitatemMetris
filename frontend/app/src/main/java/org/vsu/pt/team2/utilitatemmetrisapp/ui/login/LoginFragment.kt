package org.vsu.pt.team2.utilitatemmetrisapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.BigGeneralButtonBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentLoginBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BigGeneralButton

class LoginFragment : Fragment() {

    private var job: Job? = null
    private lateinit var button: BigGeneralButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        button = binding.bigGeneralButton.also { it.setOnClickListener(this::buttonClicked) }

        return binding.root
    }

    private fun buttonClicked(v: View) {
        job?.cancel()
        button.setStateLoading()
        job = lifecycleScope.launch {
            delay(3000L)
            button.setStateDefault()
        }
    }
}
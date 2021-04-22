package org.vsu.pt.team2.utilitatemmetrisapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentLoginBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BigGeneralButton
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.ImeActionListener
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation.EmailValidator
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation.PasswordValidator
import org.vsu.pt.team2.utilitatemmetrisapp.ui.hideKeyboard
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes

class LoginFragment : Fragment() {

    private var job: Job? = null
    private lateinit var button: BigGeneralButton
    private lateinit var emailEditText: ExtendedEditText
    private lateinit var emailTextFieldBoxes: TextFieldBoxes
    private lateinit var passwordEditText: ExtendedEditText
    private lateinit var passwordTextFieldBoxes: TextFieldBoxes

    private fun initFields(binding: FragmentLoginBinding) {
        emailTextFieldBoxes = binding.loginEmailTextfieldboxes
        passwordTextFieldBoxes = binding.loginPasswordTextfieldboxes
        emailEditText = binding.loginEmailExtendededittext
        passwordEditText = binding.loginPasswordExtendededittext.also {
            it.setOnEditorActionListener(
                ImeActionListener(ImeActionListener.Association(EditorInfo.IME_ACTION_GO) { doRequest() })
            )
        }
        button = binding.bigGeneralButton.also { it.setOnClickListener(this::buttonClicked) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        initFields(binding)
        return binding.root
    }

    private fun buttonClicked(v: View) = doRequest()

    private fun doRequest() {
        hideKeyboard()
        job?.cancel()

        job = lifecycleScope.launch {
            validateFieldsThenDo { email, password ->
                button.setStateLoading()
                println(email)
                println(password)

                //here request to server
                delay(2000L)

                button.setStateDefault()
            }
        }
    }

    private suspend fun validateFieldsThenDo(func: suspend ((String, String) -> Unit)) {
        var containsError = false
        val email = emailEditText.text.toString()
        EmailValidator.validate(email).let { result ->
            if (result.isNotBlank()) {
                emailTextFieldBoxes.setError(result, false)
                containsError = true
            }
        }
        val pass = passwordEditText.text.toString()
        PasswordValidator.validate(pass).let { result ->
            if (result.isNotBlank()) {
                passwordTextFieldBoxes.setError(result, false)
                containsError = true
            }
        }

        if (!containsError)
            func.invoke(email, pass)
    }
}
package org.vsu.pt.team2.utilitatemmetrisapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.yandex.metrica.YandexMetrica
import kotlinx.coroutines.*
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentLoginBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.AuthManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.IntentExtrasManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BigGeneralButton
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.ImeActionListener
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation.EmailValidator
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation.PasswordValidator
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MainActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.hideKeyboard
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.openActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceActivity
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.LoginViewModel
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var authManager: AuthManager

    private var job: Job? = null
    private lateinit var button: BigGeneralButton
    private lateinit var emailEditText: ExtendedEditText
    private lateinit var emailTextFieldBoxes: TextFieldBoxes
    private lateinit var passwordEditText: ExtendedEditText
    private lateinit var passwordTextFieldBoxes: TextFieldBoxes
    private val loginViewModel: LoginViewModel by activityViewModels()

    private fun initFields(binding: FragmentLoginBinding) {
        button = binding.bigGeneralButton.also {
            it.setOnClickListener(this::buttonClicked)
        }
        emailTextFieldBoxes = binding.loginEmailTextfieldboxes
        passwordTextFieldBoxes = binding.loginPasswordTextfieldboxes
        emailEditText = binding.loginEmailExtendededittext
        activity?.intent?.let {
            if (IntentExtrasManager.continueRegister.getFrom(it))
                emailEditText.setText(sessionManager.user.email)
        }
        passwordEditText = binding.loginPasswordExtendededittext.also {
            it.setOnEditorActionListener(
                ImeActionListener(ImeActionListener.Association(EditorInfo.IME_ACTION_GO) { doRequest() })
            )
        }

        loginViewModel.inLoginMode.observe(viewLifecycleOwner, {
            activity?.invalidateOptionsMenu()
            button.buttonText = if (it == true) {
                getString(R.string.auth_login_button_enter)
            } else {
                getString(R.string.auth_login_button_register)
            }
            button.invalidate()
        })
        loginViewModel.inLoginMode.postValue(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        initFields(binding)
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        binding.viewmodel = loginViewModel
        return binding.root
    }

    private fun buttonClicked(v: View) = doRequest()

    private fun doRequest() {
        hideKeyboard()
        job?.cancel()

        job = lifecycleScope.launch {
            validateFieldsThenDo { email, password ->
                button.setStateLoading()

                when (authManager.authUser(email, password)) {
                    is ApiResult.NetworkError -> {
                        //todo show error
                    }
                    is ApiResult.GenericError -> {
                        //todo show error
                    }
                    is ApiResult.Success -> {
                        (activity as? AppCompatActivity)?.replaceActivity(
                            MainActivity::class.java
                        )
                    }
                }

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (loginViewModel.inLoginMode.value == false) {
            inflater.inflate(R.menu.menu_login_to_login, menu)
        } else {
            inflater.inflate(R.menu.menu_login_to_register, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.to_login -> {
                toLoginClicked()
                true
            }
            R.id.to_register -> {
                toRegisterClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toLoginClicked() {
        loginViewModel.inLoginMode.postValue(true)
    }

    private fun toRegisterClicked() {
        loginViewModel.inLoginMode.postValue(false)
    }

    override fun onAttach(context: Context) {
        myApplication()?.appComponent?.authComponent()?.injectLoginFragment(this)
        super.onAttach(context)
    }
}
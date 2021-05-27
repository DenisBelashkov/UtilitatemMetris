package org.vsu.pt.team2.utilitatemmetrisapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentDemoUserBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.AuthManager
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BigGeneralButton
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.ImeActionListener
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation.EmailValidator
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MainActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.hideKeyboard
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.openActivity
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes
import javax.inject.Inject

class DemoUserFragment : Fragment() {

    @Inject
    lateinit var authManager: AuthManager

    private lateinit var emailEditText: ExtendedEditText
    private lateinit var emailTextFieldBoxes: TextFieldBoxes
    private var job: Job? = null
    private lateinit var button: BigGeneralButton

    private fun initFields(binding: FragmentDemoUserBinding) {
        button = binding.bigGeneralButton.also { it.setOnClickListener(this::buttonClicked) }
        emailTextFieldBoxes = binding.demoUserEmailTextfieldboxes
        emailEditText = binding.demoUserEmailExtendededittext.also {
            it.setOnEditorActionListener(
                ImeActionListener(ImeActionListener.Association(EditorInfo.IME_ACTION_GO) { doRequest() })
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDemoUserBinding.inflate(layoutInflater, container, false)
        initFields(binding)
        return binding.root
    }

    private fun buttonClicked(v: View) = doRequest()

    private fun doRequest() {
        hideKeyboard()
        job?.cancel()

        job = lifecycleScope.launch {
            validateEmailThenDo { email ->
                button.setStateLoading()
                println(email)

                when (authManager.authUser(email)) {
                    is ApiResult.NetworkError -> {
                        //todo show error
                    }
                    is ApiResult.GenericError -> {
                        //todo show error
                    }
                    is ApiResult.Success -> {
                        (activity as? AppCompatActivity)?.openActivity(
                            MainActivity::class.java,
                            true
                        )
                    }
                }

                button.setStateDefault()
            }
        }
    }

    private suspend fun validateEmailThenDo(func: suspend ((String) -> Unit)) {
        var containsError = false
        val email = emailEditText.text.toString()
        EmailValidator.validate(email).let { result ->
            if (result.isNotBlank()) {
                emailTextFieldBoxes.setError(result, false)
                containsError = true
            }
        }

        if (!containsError)
            func.invoke(email)
    }

    override fun onAttach(context: Context) {
        myApplication()?.appComponent?.authComponent()?.injectDemoUserFragment(this)
        super.onAttach(context)
    }
}
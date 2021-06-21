package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.orhanobut.logger.Logger
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSettingsBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.LoginActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.openActivity
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import javax.inject.Inject

class SettingsFragment : DisabledDrawerFragment(R.string.fragment_title_settings) {

    private val visibleChangePass = false
    private val visibleChangeEmail = false

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    fun initFields() {
        binding.visibleChangeEmail = visibleChangeEmail
        binding.visibleChangePass = visibleChangePass
        binding.email = sessionManager.user.email
        binding.settingsGeneralButtonChangeEmail.viewmodel = GeneralButtonViewModel(
            getString(R.string.settings_button_text_change_email)
        ) {
            //todo смена почты
            Logger.i("Change email clicked")
        }

        binding.settingsGeneralButtonChangePass.root.visibility =
            if (sessionManager.isDemo) View.GONE
            else View.VISIBLE

        binding.settingsGeneralButtonChangePass.viewmodel = GeneralButtonViewModel(
            getString(R.string.settings_button_text_change_password)
        ) {
            //todo смена пароля
            Logger.i("Change pass clicked")
        }

        binding.settingsGeneralButtonLeaveAccount.viewmodel = GeneralButtonViewModel(
            "Выход из аккаунта", ::onLeaveClicked
        )
    }

    override fun onAttach(context: Context) {
        myApplication()?.appComponent?.settingsComponent()?.inject(this)
        super.onAttach(context)
    }

    fun onLeaveClicked() {
        LeaveConfirmationDialogFragment({
            leave()
        }).show(
            childFragmentManager,"LeaveConfirmationDialogFragment"
        )
    }

    fun leave(){
        sessionManager.clear()
        appCompatActivity()?.openActivity(
            LoginActivity::class.java,
            true,
        )
    }

    class LeaveConfirmationDialogFragment(
        val quitAction: () -> Unit
    ) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.are_you_really_want_to_quit))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    quitAction.invoke()
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    this.dismiss()
                }
                .create()
    }
}
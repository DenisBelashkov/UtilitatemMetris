package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.*
import com.orhanobut.logger.Logger
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSettingsBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.LoginActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.openActivity
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel

class SettingsFragment : DisabledDrawerFragment(R.string.fragment_title_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.email = SessionManager.user.email
        binding.settingsGeneralButtonChangeEmail.viewmodel = GeneralButtonViewModel(
            getString(R.string.settings_button_text_change_email)
        ) {
            //todo смена почты
            Logger.i("Change email clicked")
        }

        binding.settingsGeneralButtonChangePass.root.visibility =
            if (SessionManager.isDemo) View.GONE
            else View.VISIBLE

        binding.settingsGeneralButtonChangePass.viewmodel = GeneralButtonViewModel(
            getString(R.string.settings_button_text_change_password)
        ) {
            //todo смена пароля
            Logger.i("Change pass clicked")
        }
        return binding.root
    }

    override fun onStart() {
        setHasOptionsMenu(true)
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                SessionManager.clear()
                appCompatActivity()?.openActivity(
                    LoginActivity::class.java,
                    true,
                )
            }
        }
        return true
    }
}
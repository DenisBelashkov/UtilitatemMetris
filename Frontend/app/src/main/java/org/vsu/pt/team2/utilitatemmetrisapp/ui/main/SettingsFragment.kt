package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSettingsBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel

class SettingsFragment : DisabledDrawerFragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.email = "email.email@email"
        binding.settingsGeneralButtonChangeEmail.viewmodel = GeneralButtonViewModel(
            getString(R.string.settings_button_text_change_email)
        ) {
            Logger.i("Change email clicked")
        }

        binding.settingsGeneralButtonChangePass.viewmodel = GeneralButtonViewModel(
            getString(R.string.settings_button_text_change_password)
        ) {
            Logger.i("Change pass clicked")
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }
}
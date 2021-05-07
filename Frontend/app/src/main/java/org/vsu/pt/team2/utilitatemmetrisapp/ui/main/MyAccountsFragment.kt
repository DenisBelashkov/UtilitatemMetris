package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMyAccountsBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.AccountsListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel

class MyAccountsFragment : BaseTitledFragment(R.string.fragment_title_my_accounts) {
    private lateinit var binding: FragmentMyAccountsBinding
    private val adapter = AccountsListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAccountsBinding.inflate(inflater, container, false)
        initFields(binding)
        updateAccounts()
        return binding.root
    }

    fun initFields(binding: FragmentMyAccountsBinding) {
        binding.metersListRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.metersListRecyclerView.adapter = adapter
    }

    fun updateAccounts() {
        val list = mutableListOf<AccountViewModel>().also {
            it.add(
                AccountViewModel(
                    "123455ВА8В923", "Воронеж, пр. революции, д. 1, кв 101"
                )
            )
            it.add(
                AccountViewModel(
                    "043534АВ3423А", "Воронеж, ул. Пупкина, д. 13, кв 56"
                )
            )
        }
        adapter.submitList(list)
    }
}
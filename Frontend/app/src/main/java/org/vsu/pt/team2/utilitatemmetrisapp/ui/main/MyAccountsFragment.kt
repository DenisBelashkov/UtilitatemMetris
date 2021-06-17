package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMyAccountsBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.AccountManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.BundleManager.AccountViewModelBundlePackager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.AccountsListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.genericErrorToast
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.networkConnectionErrorToast
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel
import javax.inject.Inject

class MyAccountsFragment : BaseTitledFragment(R.string.fragment_title_my_accounts) {
    private lateinit var binding: FragmentMyAccountsBinding

    @Inject
    lateinit var accountManager: AccountManager

    private val adapter = AccountsListAdapter { accountViewModel ->
        val f = AccountFragment.createWithAccount(
            Account(accountViewModel.identifier, accountViewModel.address)
        )
        replaceFragment(f)
    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myApplication()?.appComponent?.accountComponent()?.injectMyAccountsFragment(this)
    }

    fun initFields(binding: FragmentMyAccountsBinding) {
        binding.accountsListRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.accountsListRecyclerView.adapter = adapter
        binding.fragmentMyAccountsSwipeRefreshLayout.setOnRefreshListener {
            updateAccounts()
        }
    }

    fun statusLoading() {
//        binding.fragmentMyAccountStatusTv.text = "Загрузка..."
//        binding.fragmentMyAccountStatusTv.visibility = View.VISIBLE
    }

    fun statusLoaded() {
        binding.fragmentMyAccountsSwipeRefreshLayout.isRefreshing = false
        binding.fragmentMyAccountStatusTv.text = ""
        binding.fragmentMyAccountStatusTv.visibility = View.GONE
    }

    fun statusLoadedEmptyList() {
        binding.fragmentMyAccountsSwipeRefreshLayout.isRefreshing = false
        binding.fragmentMyAccountStatusTv.text = getString(R.string.dont_have_accounts)
        binding.fragmentMyAccountStatusTv.visibility = View.VISIBLE
    }

    fun statusNone() {
        binding.fragmentMyAccountsSwipeRefreshLayout.isRefreshing = false
        binding.fragmentMyAccountStatusTv.text = ""
        binding.fragmentMyAccountStatusTv.visibility = View.GONE
    }

    fun updateAccounts() {
        statusLoading()
        lifecycleScope.launchWhenCreated {
            when (val res = accountManager.accounts()) {
                is ApiResult.GenericError -> {
                    genericErrorToast(res)
                    statusNone()
                }
                is ApiResult.NetworkError -> {
                    networkConnectionErrorToast()
                    statusNone()
                }
                is ApiResult.Success -> {
                    val accounts = res.value.map {
                        AccountViewModel(
                            it.identifier,
                            it.address
                        )
                    }
                    adapter.submitList(accounts)
                    if (accounts.isEmpty())
                        statusLoadedEmptyList()
                    else
                        statusLoaded()
                }
            }
        }
    }
}
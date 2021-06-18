package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.AccountManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersWithCheckboxListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.*
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import javax.inject.Inject

class AccountFragment : DisabledDrawerFragment() {

    private lateinit var binding: FragmentAccountBinding

    val account: Account by AccountFragment.createFragmentArgs.asProperty()

    @Inject
    lateinit var meterManager: MeterManager

    @Inject
    lateinit var accountManager: AccountManager

    private val adapter = MetersWithCheckboxListAdapter().apply {
        callbackOnItemLongClick = {
            lifecycleScope.launch {
                val f = MeterFragment.createWithMeterIdentifier(it.identifier)
                replaceFragment(f)
            }
        }
        callbackOnCheckedItemsChanged = { checked ->
            val sum = checked.sumOf { it.backlog }
            binding.sumForPay = sum
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        initFields(binding)
        loadAccountMeters()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myApplication()?.appComponent?.accountComponent()?.injectAccountFragment(this)
    }

    fun initFields(binding: FragmentAccountBinding) {
        binding.sumForPay = 0.0
        binding.accountAddress = account.address
        title = getString(R.string.account, account.identifier) //+ account.identifier
        binding.payChosenMetersButton.viewmodel =
            GeneralButtonViewModel(getString(R.string.pay_for_chosen)) {
                appCompatActivity()?.replaceFragment(
                    PaymentFragment.createWithMetersIdentifier(
                        adapter.getChecked().map { it.identifier }
                    )
                )
            }
        binding.metersOnAccountRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.metersOnAccountRecyclerView.adapter = adapter
        binding.fragmentAccountMetersSwipeRefreshLayout.setOnRefreshListener {
            loadAccountMeters()
        }
    }

    private fun statusLoadedAccounts() {
        binding.fragmentAccountMetersSwipeRefreshLayout.isRefreshing = false
        binding.fragmentAccountStatusTv.visibility = View.GONE
        binding.fragmentAccountStatusTv.text = ""
    }

    private fun statusLoadedAccountsEmptyList() {
        binding.fragmentAccountMetersSwipeRefreshLayout.isRefreshing = false
        binding.fragmentAccountStatusTv.visibility = View.VISIBLE
        binding.fragmentAccountStatusTv.text = getString(R.string.account_dont_has_meters)
    }

    private fun statusAccountsNone() {
        binding.fragmentAccountMetersSwipeRefreshLayout.isRefreshing = false
        binding.fragmentAccountStatusTv.visibility = View.GONE
        binding.fragmentAccountStatusTv.text = ""
    }

    fun loadAccountMeters() {
        lifecycleScope.launch {
            when (val res = meterManager.getMetersByAccountIdentifier(account.identifier)) {
                is ApiResult.GenericError -> {
                    genericErrorToast(res)
                    statusAccountsNone()
                }
                is ApiResult.NetworkError -> {
                    networkConnectionErrorToast()
                    statusAccountsNone()
                }
                is ApiResult.Success -> {
                    val metersList =
                        res.value.map { MeterItemViewModel(it.identifier, it.type, it.balance) }
                    adapter.submitList(metersList)
                    if (metersList.isEmpty())
                        statusLoadedAccountsEmptyList()
                    else
                        statusLoadedAccounts()
                }
            }
        }
    }

    companion object {
        val createFragmentArgs = CreationFragmentArgs<Account>(
            { acc, bundle ->
                bundle.putString("account_identifier", acc.identifier)
                bundle.putString("account_address", acc.address)
                bundle
            },
            { bundle ->
                val identifier =
                    bundle.getString("account_identifier", null) ?: return@CreationFragmentArgs null
                val address =
                    bundle.getString("account_address", null) ?: return@CreationFragmentArgs null
                Account(identifier, address)
            }
        )

        fun createWithAccount(account: Account): AccountFragment {
            return createFragmentArgs.fill(AccountFragment(), account)
        }
    }
}
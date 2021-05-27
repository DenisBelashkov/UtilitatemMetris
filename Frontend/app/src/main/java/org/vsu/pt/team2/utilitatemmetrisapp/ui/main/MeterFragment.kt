package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.CreationFragmentArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.requireAppCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.requireMyApplication
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import javax.inject.Inject


class MeterFragment : DisabledDrawerFragment(R.string.fragment_title_meter) {
    private lateinit var binding: FragmentMeterBinding
    private val meterIdentifier by MeterFragment.creationFragmentArgs.asProperty()
    private var meter: Meter? = null
    private var isSaved: Boolean = false
    private var menu: Menu? = null

    @Inject
    lateinit var meterManager: MeterManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields(binding)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meter_menu, menu)
        this.menu = menu
        this.menu?.findItem(R.id.meter_menu_fav)?.let {
            setMenuItemState(isSaved, it)
        }
    }

    private fun setMenuItemState(isMeterSaved: Boolean, item: MenuItem) {
        item.isChecked = isMeterSaved

        if (isMeterSaved)
            item.setIcon(R.drawable.ic_star_filled_24)
        else
            item.setIcon(R.drawable.ic_star_outline_24)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meter_menu_fav -> {
                val changedValue = !item.isChecked

                lifecycleScope.launch {
                    val success = changeFav(changedValue)


                    if (success) {
                        setMenuItemState(changedValue, item)
                    }
//                        item.isChecked = changedValue
//
//
//                    if (item.isChecked)
//                        item.setIcon(R.drawable.ic_star_filled_24)
//                    else
//                        item.setIcon(R.drawable.ic_star_outline_24)
                }
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    /**
     * @param isFav value to set
     * @return is successfully changed
     */
    suspend fun changeFav(isFav: Boolean): Boolean {
        meter?.let {
            val res = if (isFav)
                meterManager.saveMeter(it.identifier)
            else
                meterManager.deleteMeter(it.identifier)
            return when (res) {
                is ApiResult.Success ->
                    true
                else ->
                    false
            }
        }
        return false
    }

    fun initFields(binding: FragmentMeterBinding) {
//        arguments?.let { bundle ->
//            BundleManager.MeterViewModelBundlePackager.getFrom(bundle)
//                ?.let {
//                    binding.setFromVM(it, requireContext())
//                } ?: parentFragmentManager.popBackStack()
//        }
        lifecycleScope.launchWhenCreated {
            try {
                val res = meterManager.getMeterByIdentifier(meterIdentifier)
                when (res) {
                    is ApiResult.NetworkError, is ApiResult.GenericError -> {
                        //show toast
                        parentFragmentManager.popBackStack()
                    }
                    is ApiResult.Success -> {
                        meter = res.value.first
                        binding.setFromVM(
                            MeterViewModel.fromMeter(res.value.first),
                            requireContext()
                        )
                        isSaved = res.value.second
                        menu?.findItem(R.id.meter_menu_fav)?.let {
                            setMenuItemState(isSaved, it)
                        }

                    }
                }
            } catch (npe: NullPointerException) {
                parentFragmentManager.popBackStack()
            }

        }

        binding.meterShowHistory.viewmodel = GeneralButtonViewModel(
            getString(R.string.show_payment_history),
            {
                //todo переход на экран "истории платежей"
            }
        )
        binding.meterPayBacklog.viewmodel = GeneralButtonViewModel(
            getString(R.string.pay_backlog),
            {
                //todo переход на экран "погасить задолженность"
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireAppCompatActivity().requireMyApplication().appComponent?.meterComponent()
            ?.injectMeterFragment(this)
    }

    companion object {
        private val creationFragmentArgs = CreationFragmentArgs<String>(
            { meterIdentifier, bundle ->
                bundle.putString("Ident", meterIdentifier)
                bundle
            },
            { bundle ->
                bundle.getString("Ident", "")
            }
        )

        fun createWithMeterIdentifier(identifier: String): MeterFragment {
            return creationFragmentArgs.fill(MeterFragment(), identifier)
        }
    }
}
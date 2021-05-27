package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.BundleManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.CreationFragmentArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.bundleArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.requireAppCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.requireMyApplication
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import java.lang.NullPointerException
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class MeterFragment : DisabledDrawerFragment(R.string.fragment_title_meter) {
    private lateinit var binding: FragmentMeterBinding
    private val meterIdentifier by MeterFragment.creationFragmentArgs.asProperty()
    private var meter: Meter? = null

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meter_menu_fav -> {
                val changedValue = !item.isChecked

                lifecycleScope.launch {
                    val success = changeFav(changedValue)

                    if (success)
                        item.isChecked = changedValue


                    if (item.isChecked)
                        item.setIcon(R.drawable.ic_star_filled_24)
                    else
                        item.setIcon(R.drawable.ic_star_outline_24)
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
            val res = meterManager.changeMeterFavorite(
                it,
                isFav
            )
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
                meter = meterManager.requiredMeter(meterIdentifier).also {
                    binding.setFromVM(MeterViewModel.fromMeter(it), requireContext())
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
        requireAppCompatActivity().requireMyApplication().appComponent?.meterComponent()?.injectMeterFragment(this)
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
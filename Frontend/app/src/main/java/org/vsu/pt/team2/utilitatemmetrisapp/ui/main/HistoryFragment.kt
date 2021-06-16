package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentHistoryBinding
import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter
import org.vsu.pt.team2.utilitatemmetrisapp.managers.PaymentManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentsFilter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersHistoryAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.CreationFragmentArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.genericErrorToast
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.networkConnectionErrorToast
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.HistoryMeterItemViewModel
import javax.inject.Inject

class HistoryFragment : BaseTitledFragment(R.string.fragment_history_title) {
    private lateinit var binding: FragmentHistoryBinding
    private val adapter = MetersHistoryAdapter()

    private val paymentsFilter: PaymentsFilter by creationFragmentArgs.asProperty()

    @Inject
    lateinit var paymentManager: PaymentManager

//    private fun openPaymentHistoryFragmentForMeterItem(historyMeterItem: HistoryMeterItemViewModel) {
//        //todo Найти квитанцию, в которой находится этот счётчик
//        //открыть фрагмент с данной квитанцией
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields(binding)
        loadData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myApplication()?.appComponent?.paymentComponent()?.injectHistoryFragment(this)
    }

    private fun initFields(binding: FragmentHistoryBinding) {

        binding.historyMetersListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyMetersListRecyclerView.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launchWhenStarted {
            when (val res = paymentManager.paymentHistory(paymentsFilter)) {
                is ApiResult.NetworkError ->
                    networkConnectionErrorToast()
                is ApiResult.GenericError ->
                    genericErrorToast(res)
                is ApiResult.Success -> {
                    val resultList: List<HistoryMeterItemViewModel> = res.value.flatMap { pData ->
                        val paymentItemList: List<HistoryMeterItemViewModel> =
                            pData.metricDatas.map { pMeterData ->
                                HistoryMeterItemViewModel(
                                    pMeterData.meter.identifier,
                                    pMeterData.meter.type,
                                    pMeterData.cost,
                                    pData.date,
                                    pMeterData.meter.address
                                )
                            }
                        paymentItemList
                    }
                    adapter.submitList(resultList)
                }
            }
        }

        //todo захардкоженые данные убрать
//        val data = listOf<HistoryMeterItemViewModel>(
//            HistoryMeterItemViewModel(
//                "765432bhjdskf",
//                MeterType.ColdWater,
//                123.3,
//                "2014:01:12",
//                "Ленинский проспект, 102, кв 14"
//            ),
//            HistoryMeterItemViewModel(
//                "345hj345h34",
//                MeterType.Elect,
//                3423.9,
//                "2014:01:12",
//                "Ленинский проспект, 102, кв 14"
//            ),
//            HistoryMeterItemViewModel(
//                "j7k567kj56321",
//                MeterType.Heating,
//                1029.9,
//                "2014:01:12",
//                "Ленинский проспект, 102, кв 14"
//            ),
//        )
//
//        adapter.submitList(data)
    }

    companion object {
        fun createWithFilter(filter: PaymentsFilter): HistoryFragment {
            return HistoryFragment.creationFragmentArgs.fill(HistoryFragment(), filter)
        }

        val creationFragmentArgs = CreationFragmentArgs<PaymentsFilter>(
            { filter, bundle ->
                filter.identifierMetric?.let {
                    bundle.putString("f_identifier", it)
                }
                filter.dateFrom?.let {
                    bundle.putString("f_dateFrom", DateFormatter.toString(it))
                }
                filter.dateTo?.let {
                    bundle.putString("f_dateTo", DateFormatter.toString(it))
                }
                filter.meterType?.let {
                    bundle.putString("f_meterType", it.toValue())
                }
                bundle
            },
            {
                val filter = PaymentsFilter()
                it.getString("f_identifier", "")?.let {
                    if (it.isNotBlank())
                        filter.identifierMetric = it
                }
                it.getString("f_dateFrom", "")?.let {
                    if (it.isNotBlank())
                        filter.dateFrom = DateFormatter.fromString(it)
                }
                it.getString("f_dateTo", "")?.let {
                    if (it.isNotBlank())
                        filter.dateTo = DateFormatter.fromString(it)
                }
                it.getString("f_meterType", "")?.let {
                    if (it.isNotBlank())
                        filter.meterType = MeterType.forValue(it)
                }

                filter
            }
        )
    }
}
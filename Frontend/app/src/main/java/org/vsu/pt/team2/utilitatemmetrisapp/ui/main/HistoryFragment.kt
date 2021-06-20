package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentDialogFilterHistoryBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentHistoryBinding
import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter
import org.vsu.pt.team2.utilitatemmetrisapp.managers.PaymentManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentData
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentMetricData
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentsFilter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersHistoryAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.CustomDateTimePicker
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.*
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.HistoryMeterItemViewModel
import java.util.*
import javax.inject.Inject

class HistoryFragment : BaseTitledFragment(R.string.fragment_history_title) {
    private lateinit var binding: FragmentHistoryBinding
    private val adapter = MetersHistoryAdapter {
        replaceFragment(PaymentFromHistoryFragment.createWithPaymentId(it.paymentId))
    }

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
        setHasOptionsMenu(true)
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_menu_open_filter -> {
                openFilterClicked()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myApplication()?.appComponent?.paymentComponent()?.injectHistoryFragment(this)
    }

    private fun initFields(binding: FragmentHistoryBinding) {

        binding.historyMetersListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyMetersListRecyclerView.adapter = adapter
        binding.fragmentHistorySwipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
    }

    private fun statusLoading() {
//        binding.fragmentHistoryStatusTv.text = "Загрузка..."
//        binding.fragmentHistoryStatusTv.visibility = View.GONE
    }

    private fun statusLoaded() {
        binding.fragmentHistorySwipeRefreshLayout.isRefreshing = false
        binding.fragmentHistoryStatusTv.text = ""
        binding.fragmentHistoryStatusTv.visibility = View.GONE
    }

    private fun statusLoadedEmptyList() {
        binding.fragmentHistorySwipeRefreshLayout.isRefreshing = false
        binding.fragmentHistoryStatusTv.text = getString(R.string.dont_have_payments)
        binding.fragmentHistoryStatusTv.visibility = View.VISIBLE
    }

    private fun statusNone() {
        binding.fragmentHistorySwipeRefreshLayout.isRefreshing = false
        binding.fragmentHistoryStatusTv.text = ""
        binding.fragmentHistoryStatusTv.visibility = View.GONE
    }

    private fun loadData() {
        statusLoading()
        lifecycleScope.launch {
            when (val res = paymentManager.paymentHistory(paymentsFilter)) {
                is ApiResult.NetworkError -> {
                    statusNone()
                    networkConnectionErrorToast()
                }
                is ApiResult.GenericError -> {
                    statusNone()
                    genericErrorToast(res)
                }
                is ApiResult.Success -> {
                    submitListFromResponseAndFilter(res, paymentsFilter)
                }
            }
        }
    }

    private fun submitListFromResponseAndFilter(
        res: ApiResult.Success<List<PaymentData>>,
        filter: PaymentsFilter
    ) {

        val filterFunc: (PaymentMetricData) -> Boolean = { pmd ->
            if (filter.identifierMetric != null) {
                pmd.meter.identifier == filter.identifierMetric
            } else if (filter.meterType != null) {
                pmd.meter.type == filter.meterType
            } else {
                true
            }
        }
        val resultList: List<HistoryMeterItemViewModel> = res.value.flatMap { pData ->
            val paymentItemList: List<HistoryMeterItemViewModel> =
                pData.metricDatas
                    .filter { filterFunc.invoke(it) }
                    .map { pMeterData ->
                        HistoryMeterItemViewModel(
                            pData.id,
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
        if (resultList.isEmpty())
            statusLoadedEmptyList()
        else
            statusLoaded()
    }

    fun openFilterClicked() {
        FilterDialog(
            paymentsFilter,
            { from, to, type ->
                this.paymentsFilter.dateFrom = from?.time
                this.paymentsFilter.dateFrom = to?.time
                this.paymentsFilter.meterType = type
                loadData()
            }
        ).show(parentFragmentManager, "FilterHistoryDialogFragment")
    }

    class FilterDialog(
        val filter: PaymentsFilter,
        val onFilterReady: (from: Calendar?, to: Calendar?, MeterType?) -> Unit
    ) : DialogFragment() {
        lateinit var binding: FragmentDialogFilterHistoryBinding

        private val meterTypeValues = MeterType.values()
        private lateinit var meterTypeValuesStr: List<String>

        private var meterType: MeterType? = null
        private var dateFrom: Calendar? = null
        private var dateTo: Calendar? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
            binding = FragmentDialogFilterHistoryBinding.inflate(
                inflater,
                container,
                false
            )

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            context?.let { ctx ->
                binding.shouldForceIdentifier = this.filter.identifierMetric != null
                binding.forceIdentifier = this.filter.identifierMetric.toString()
                meterTypeValuesStr = MeterType.values().map { it.toLanguagedString(ctx) }
                    .plus(getString(R.string.Any))
                binding.filterDialogMeterTypeSpinner.adapter =
                    ArrayAdapter<String>(
                        ctx,
                        android.R.layout.simple_spinner_dropdown_item,
                        meterTypeValuesStr
                    )
                binding.filterDialogMeterTypeSpinner.setSelection(meterTypeValues.size)
                binding.filterDialogMeterTypeSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == meterTypeValues.size)
                                meterType = null
                            else
                                meterType = meterTypeValues[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            meterType = null
                        }

                    }

                filter.dateFrom?.let {
                    binding.filterDialogDateFromDatepickerBtn.text = DateFormatter.toString(it)
                }

                filter.dateTo?.let {
                    binding.filterDialogDateToDatepickerBtn.text = DateFormatter.toString(it)
                }

                filter.meterType?.let {
                    binding.filterDialogMeterTypeSpinner.setSelection(meterTypeValues.indexOf(it))
                }

                binding.filterDialogDateFromDatepickerBtn.setOnClickListener {
                    CustomDateTimePicker(ctx, {
                        this.dateFrom = it
                        binding.filterDialogDateFromDatepickerBtn.text =
                            DateFormatter.toString(it.time)
                    }).show()
                }
                binding.filterDialogDateToDatepickerBtn.setOnClickListener {
                    CustomDateTimePicker(ctx, {
                        this.dateTo = it
                        binding.filterDialogDateToDatepickerBtn.text =
                            DateFormatter.toString(it.time)
                    }).show()
                }
            }


        }

        override fun onDismiss(dialog: DialogInterface) {
            super.onDismiss(dialog)
            onFilterReady.invoke(dateFrom, dateTo, meterType)
        }

        override fun onStart() {
            super.onStart()
//            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
//            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
//            dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
        }
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
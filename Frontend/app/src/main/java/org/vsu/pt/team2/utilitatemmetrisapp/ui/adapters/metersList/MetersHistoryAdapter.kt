package org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterHistoryBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.HistoryMeterItemViewModel

class MetersHistoryAdapter(
        val onMeterClickCallback: (HistoryMeterItemViewModel) -> Unit = {}
) : ListAdapter<HistoryMeterItemViewModel, MetersHistoryAdapter.MeterHistoryViewHolder>(
        MeterHistoryDiffCallback()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterHistoryViewHolder {
        return MeterHistoryViewHolder(
                ItemMeterHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onMeterClickCallback
        )
    }

    override fun onBindViewHolder(holder: MeterHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeterHistoryViewHolder(
            val binding: ItemMeterHistoryBinding,
            private val onMeterClickCallback: (HistoryMeterItemViewModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryMeterItemViewModel) = with(itemView) {
            binding.setFromVM(item, context)

            setOnClickListener {
                onMeterClickCallback.invoke(item)
            }
        }
    }

    class MeterHistoryDiffCallback : DiffUtil.ItemCallback<HistoryMeterItemViewModel>() {
        override fun areItemsTheSame(
                oldItem: HistoryMeterItemViewModel,
                newItem: HistoryMeterItemViewModel
        ): Boolean {
            return oldItem.identifier === newItem.identifier
        }

        override fun areContentsTheSame(
                oldItem: HistoryMeterItemViewModel,
                newItem: HistoryMeterItemViewModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}
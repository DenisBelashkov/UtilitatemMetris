package org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterWithCheckboxBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class MetersListAdapter : ListAdapter<MeterViewModel, MetersListAdapter.MeterViewHolder>(
    MeterDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterViewHolder {
        return MeterViewHolder(
            ItemMeterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MeterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeterViewHolder(
        val binding: ItemMeterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MeterViewModel) = with(itemView) {
            binding.setFromVM(item,context)

            setOnClickListener {
                //todo переход на страницу с счётчиком
            }
        }
    }

    class MeterDiffCallback : DiffUtil.ItemCallback<MeterViewModel>() {
        override fun areItemsTheSame(oldItem: MeterViewModel, newItem: MeterViewModel): Boolean {
            return oldItem.identifier === newItem.identifier
        }

        override fun areContentsTheSame(oldItem: MeterViewModel, newItem: MeterViewModel): Boolean {
            return oldItem == newItem
        }
    }
}
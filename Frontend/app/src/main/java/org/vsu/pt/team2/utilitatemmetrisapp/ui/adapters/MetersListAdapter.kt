package org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class MetersListAdapter : ListAdapter<MeterViewModel, MetersListAdapter.MeterViewHolder>(
    MeterDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterViewHolder {
        return MeterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_meter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MeterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemMeterBinding? = null
        fun bind(item: MeterViewModel) = with(itemView) {

            binding = ItemMeterBinding.bind(itemView).apply {
                meterType = item.type.name
                this.setMeterIdentifier(item.identifier)
                this.setBacklogValue(item.backlog)
                checkbox.isChecked = false
            }

            setOnClickListener {
                binding?.checkbox?.let {
                    binding?.checkbox?.isChecked = !it.isChecked
                }
            }
        }
    }

    class MeterDiffCallback : DiffUtil.ItemCallback<MeterViewModel>() {
        override fun areItemsTheSame(oldItem: MeterViewModel, newItem: MeterViewModel): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(oldItem: MeterViewModel, newItem: MeterViewModel): Boolean {
            return oldItem == newItem
        }
    }
}
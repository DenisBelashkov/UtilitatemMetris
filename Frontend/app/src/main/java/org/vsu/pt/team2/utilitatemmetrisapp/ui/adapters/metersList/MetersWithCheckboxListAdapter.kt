package org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterWithCheckboxBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel

class MetersWithCheckboxListAdapter(
    val onMeterClickCallback: (MeterItemViewModel) -> Unit = {}
) :
    ListAdapter<MeterItemViewModel, MetersWithCheckboxListAdapter.MeterViewHolder>(
        MeterDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterViewHolder {
        return MeterViewHolder(
            ItemMeterWithCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onMeterClickCallback
        )
    }

    override fun onBindViewHolder(holder: MeterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getChecked(): List<MeterItemViewModel> {
        //todo вертать чекнутые
        return emptyList()
    }

    class MeterViewHolder(
        val binding: ItemMeterWithCheckboxBinding,
        private val onMeterClickCallback: (MeterItemViewModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MeterItemViewModel) = with(itemView) {
            binding.apply {
                setFromVM(item, context)
                checkbox.isChecked = false
            }

            setOnClickListener {
                binding.checkbox.let {
                    it.isChecked = !it.isChecked
                }
            }

            setOnLongClickListener {
                onMeterClickCallback.invoke(item)
                true
            }
        }
    }

    class MeterDiffCallback : DiffUtil.ItemCallback<MeterItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: MeterItemViewModel,
            newItem: MeterItemViewModel
        ): Boolean {
            return oldItem.identifier === newItem.identifier
        }

        override fun areContentsTheSame(
            oldItem: MeterItemViewModel,
            newItem: MeterItemViewModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
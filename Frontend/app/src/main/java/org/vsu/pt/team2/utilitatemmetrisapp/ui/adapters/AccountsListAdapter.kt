package org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel

class AccountsListAdapter : ListAdapter<AccountViewModel, AccountsListAdapter.AccountViewHolder>(
    AccountDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AccountViewHolder(
        val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AccountViewModel) = with(itemView) {

            binding.apply {
                this.identifier = item.identifier
                this.address = item.address
            }

            setOnClickListener {

            }
        }
    }

    class AccountDiffCallback : DiffUtil.ItemCallback<AccountViewModel>() {
        override fun areItemsTheSame(
            oldItem: AccountViewModel,
            newItem: AccountViewModel
        ): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(
            oldItem: AccountViewModel,
            newItem: AccountViewModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
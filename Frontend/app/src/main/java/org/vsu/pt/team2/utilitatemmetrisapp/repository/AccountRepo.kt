package org.vsu.pt.team2.utilitatemmetrisapp.repository

import org.vsu.pt.team2.utilitatemmetrisapp.models.Account


class AccountRepo {
    private val accounts = mutableListOf<Account>()

    fun clear() {
        accounts.clear()
    }

    fun addAccount(acc: Account) {
        accounts.add(acc)
    }

    fun addAccounts(accs: List<Account>) {
        accounts.addAll(accs)
    }

    fun deleteAccount(acc: Account) {
        accounts.remove(acc)
    }

    fun deleteAccount(identifier: String) {
        accounts.find { it.identifier == identifier }?.let {
            deleteAccount(it)
        }
    }

}
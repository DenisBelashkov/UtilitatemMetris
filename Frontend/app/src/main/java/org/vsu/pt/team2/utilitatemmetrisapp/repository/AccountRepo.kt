package org.vsu.pt.team2.utilitatemmetrisapp.repository

import org.vsu.pt.team2.utilitatemmetrisapp.models.Account


class AccountRepo {
    //todo если будет реальный репозиторий с бд, то сделать:
    /*
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO
         withContext(dispatcher) {
            //code
         }
     */

    private val accounts = mutableListOf<Account>()

    fun accounts(): List<Account> = accounts

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
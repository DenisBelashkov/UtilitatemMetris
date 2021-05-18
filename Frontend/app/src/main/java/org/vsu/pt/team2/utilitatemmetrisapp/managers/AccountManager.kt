package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.BaseWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject
import javax.inject.Singleton

class AccountManager @Inject constructor(
    val baseWorker: BaseWorker,
    val accountRepo: AccountRepo
) {

    suspend fun updateAccounts(userId: Int) {
        val accsApiRes = baseWorker.accounts(userId)
        if (accsApiRes.isSuccess()) {
            accsApiRes.data?.let {
                accountRepo.clear()
                accountRepo.addAccounts(it.map { Account(it) })
            }
        }
    }

    suspend fun getAccounts(): List<Account> {
        return accountRepo.accounts()
    }

}
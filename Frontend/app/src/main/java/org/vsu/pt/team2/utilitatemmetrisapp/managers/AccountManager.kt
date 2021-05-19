package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import javax.inject.Inject

class AccountManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val accountRepo: AccountRepo
) {

    suspend fun updateAccounts(userId: Int) {
        val accsApiRes = generalWorker.accounts(userId)
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
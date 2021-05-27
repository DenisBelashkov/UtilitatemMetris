package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.offlineTools.AccountManagerOfflineSupport
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import javax.inject.Inject

class AccountManager @Inject constructor(
    val accountManagerOfflineSupport: AccountManagerOfflineSupport,
    val accountRepo: AccountRepo
) {

    suspend fun accounts(): ApiResult<List<Account>> {
        val res = accountManagerOfflineSupport.flats()
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val accounts = res.value.map { Account(it) }
                accountRepo.clear()
                accountRepo.addAccounts(accounts)

                ApiResult.Success(accounts)
            }
        }
    }

}
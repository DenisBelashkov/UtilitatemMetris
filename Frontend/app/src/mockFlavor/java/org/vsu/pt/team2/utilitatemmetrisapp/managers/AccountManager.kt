package org.vsu.pt.team2.utilitatemmetrisapp.managers

import kotlinx.coroutines.delay
import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import javax.inject.Inject

class AccountManager @Inject constructor(
    val accountRepo: AccountRepo
) {

    suspend fun accounts(): ApiResult<List<Account>> {
        //Изначально при запуске офлайн приложения в accountRepo сгенерятся счета юзера
        delay(400)
        val res: ApiResult<List<Account>> =
            ApiResult.Success<List<Account>>(accountRepo.accounts().toList())
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val accounts = res.value
//                accountRepo.clear()
//                accountRepo.addAccounts(accounts)

                ApiResult.Success(accounts)
            }
        }
    }

}
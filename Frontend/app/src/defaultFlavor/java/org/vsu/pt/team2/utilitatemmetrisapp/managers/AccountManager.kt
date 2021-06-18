package org.vsu.pt.team2.utilitatemmetrisapp.managers

import com.orhanobut.logger.Logger
import org.vsu.pt.team2.utilitatemmetrisapp.models.Account
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.AccountRepo
import javax.inject.Inject

class AccountManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val accountRepo: AccountRepo
) {

    suspend fun accounts(): ApiResult<List<Account>> {
        Logger.d("Загрузка счетов юзера")
        return when (val res = generalWorker.flats()) {
            is ApiResult.NetworkError -> {
                Logger.d("Загрузка счетов юзера, ошибка")
                res
            }
            is ApiResult.GenericError -> {
                Logger.d("Загрузка счетов юзера, ошибка. ${res.code}")
                res
            }
            is ApiResult.Success -> {
                val accounts = res.value.map { Account(it) }
                Logger.d("Загрузка счетов юзера, успех. $accounts")
                accountRepo.clear()
                accountRepo.addAccounts(accounts)

                ApiResult.Success(accounts)
            }
        }
    }

}
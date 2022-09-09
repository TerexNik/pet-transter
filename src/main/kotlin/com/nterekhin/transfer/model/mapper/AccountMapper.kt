package com.nterekhin.transfer.model.mapper

import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.entity.Account
import org.springframework.stereotype.Component

@Component
class AccountMapper {
    fun toDTO(account: Account) = AccountDTO(
        id = account.id,
        name = account.name,
        surname = account.surname,
        createDate = account.createDate,
        lastUpdate = account.lastUpdate,
        balance = account.balance,
    )
}
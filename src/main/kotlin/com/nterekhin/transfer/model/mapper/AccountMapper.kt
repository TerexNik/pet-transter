package com.nterekhin.transfer.model.mapper

import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.entity.Account
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.persistence.Column

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

    fun toEntity(accountDTO: AccountDTO) = Account(
        name = accountDTO.name,
        surname = accountDTO.surname,
        createDate = LocalDateTime.now(),
        lastUpdate = LocalDateTime.now(),
        balance = accountDTO.balance,
    )
}
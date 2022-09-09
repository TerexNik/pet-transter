package com.nterekhin.transfer.service

import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.mapper.AccountMapper
import com.nterekhin.transfer.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val repository: AccountRepository,
    private val mapper: AccountMapper
) {
    fun getAccount(id: Int): AccountDTO? {
        val account = repository.findById(id)
        return if (account.isPresent) {
            mapper.toDTO(account.get())
        } else {
            null
        }
    }
}
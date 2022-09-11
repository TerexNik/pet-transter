package com.nterekhin.transfer.service

import com.nterekhin.transfer.error.LackOfFundsError
import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.mapper.AccountMapper
import com.nterekhin.transfer.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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

    fun createAccount(accountDTO: AccountDTO): AccountDTO {
        val entity = mapper.toEntity(accountDTO)
        val saved = repository.save(entity)
        return mapper.toDTO(saved)
    }

    @Transactional
    fun transfer(from: Int, to: Int, amount: Double): AccountDTO? {
        val accounts = repository.findAllById(setOf(from, to))
        val accountFrom = accounts.firstOrNull { it.id == from }
        val accountTo = accounts.firstOrNull { it.id == to }
        return if (accountFrom != null && accountTo != null) {
            if (accountFrom.balance!! < amount) {
                throw LackOfFundsError("Account ${accountFrom.id} does not have enough balance")
            }
            accountFrom.balance = accountFrom.balance?.minus(amount)
            accountTo.balance = accountTo.balance?.plus(amount)
            accountFrom.lastUpdate = LocalDateTime.now()
            accountTo.lastUpdate = LocalDateTime.now()
            val saved = repository.saveAll(setOf(accountFrom, accountTo))
            saved.firstOrNull { it.id == from }?.let { mapper.toDTO(it) }
        } else {
            null
        }

    }
}
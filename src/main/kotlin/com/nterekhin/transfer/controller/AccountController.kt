package com.nterekhin.transfer.controller

import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.service.AccountService
import com.nterekhin.transfer.util.CommonHeaders
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    private val service: AccountService
) {

    private val log = LoggerFactory.getLogger(AccountController::class.java)


    @GetMapping("/account/{id}")
    fun getAccount(
        @PathVariable id: Int
    ): ResponseEntity<AccountDTO> {
        val result = service.getAccount(id)
        return if (result != null) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.noContent()
                .header(CommonHeaders.X_ERROR_MESSAGE, "No such account")
                .build()
        }
    }

}
package com.nterekhin.transfer.controller

import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.service.AccountService
import com.nterekhin.transfer.util.CommonHeaders
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(
    private val service: AccountService
) {

    private val log = LoggerFactory.getLogger(AccountController::class.java)

    @GetMapping("/{id}")
    fun getAccount(
        @PathVariable id: Int
    ): ResponseEntity<AccountDTO> {
        val result = service.getAccount(id)
        return if (result != null) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.notFound()
                .header(CommonHeaders.X_ERROR_MESSAGE, "No such account")
                .build()
        }
    }

    @PostMapping
    fun createAccount(
        @RequestBody accountDTO: AccountDTO
    ): ResponseEntity<AccountDTO> {
        val result = service.createAccount(accountDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }
}
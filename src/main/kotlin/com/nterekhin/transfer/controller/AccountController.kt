package com.nterekhin.transfer.controller

import com.nterekhin.transfer.error.LackOfFundsError
import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.service.AccountService
import com.nterekhin.transfer.util.CommonHeaders
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/account")
class AccountController(
    private val service: AccountService
) {

    companion object {
        private val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

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

    @PostMapping("/transfer")
    fun transferBalance(
        @RequestParam from: Int,
        @RequestParam to: Int,
        @RequestBody amount: Double
    ): ResponseEntity<AccountDTO> {
        try {
            val result = service.transfer(from, to, amount)
            return if (result != null) {
                ResponseEntity.ok()
                    .header(HttpHeaders.LAST_MODIFIED, result.lastUpdate?.format(dtf))
                    .body(result)
            } else {
                ResponseEntity.notFound()
                    .header(CommonHeaders.X_ERROR_MESSAGE, "No such account")
                    .build()
            }
        } catch (e: LackOfFundsError) {
            return ResponseEntity.badRequest()
                .header(CommonHeaders.X_ERROR_MESSAGE, e.message)
                .build()
        }
    }
}
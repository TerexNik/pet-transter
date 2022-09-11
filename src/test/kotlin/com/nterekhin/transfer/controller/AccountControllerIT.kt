package com.nterekhin.transfer.controller

import com.nterekhin.transfer.FullContextTest
import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.entity.Account
import com.nterekhin.transfer.repository.AccountRepository
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AutoConfigureWebTestClient
class AccountControllerIT : FullContextTest() {

    private val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @Autowired
    lateinit var repository: AccountRepository

    @Autowired
    lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun init() {
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }


    @Test
    fun shouldGetAccountDetails() {
        val account = Account(
            name = "John",
            surname = "Smit",
            createDate = LocalDateTime.of(2022, 9, 9, 19, 30, 5),
            lastUpdate = LocalDateTime.of(2022, 9, 9, 19, 30, 5),
            balance = 345.0321,
        )
        val saved = repository.save(account)

        webTestClient.get().uri("/account/${saved.id}").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo(account.name ?: "")
            .jsonPath("$.surname").isEqualTo(account.surname ?: "")
            .jsonPath("$.createDate").isEqualTo("2022-09-09T19:30:05.000Z")
            .jsonPath("$.lastUpdate").isEqualTo("2022-09-09T19:30:05.000Z")
            .jsonPath("$.balance").isEqualTo(account.balance ?: "")
    }

    @Test
    fun shouldGetNoContent() {
        webTestClient.get().uri("/account/2").exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun shouldSaveAccount() {
        val account = AccountDTO(
            name = "William",
            surname = "Shwarts",
            balance = 120500.3971,
        )

        webTestClient.post().uri("/account")
            .body(Mono.just(account)).exchange()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.name").isEqualTo("William")
            .jsonPath("$.surname").isEqualTo("Shwarts")
            .jsonPath("$.balance").isEqualTo(120500.3971)
    }

    @Test
    fun shouldTransferBalance() {
        //given
        val accountFrom = repository.save(
            Account(
                name = "William",
                surname = "Shwarts",
                balance = 120500.3971,
            )
        )
        val accountTo = repository.save(
            Account(
                name = "John",
                surname = "Smit",
                balance = 345.0321,
            )
        )
        val amount = 500.09
        //when
        //then
        webTestClient.post().uri("/account/transfer?from={accountId}&to={accountId}", accountFrom.id, accountTo.id)
            .body(Mono.just(amount)).exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader()
            .value(HttpHeaders.LAST_MODIFIED, Matchers.greaterThan(accountTo.lastUpdate?.format(dtf) ?: ""))
            .expectBody()
            .jsonPath("$.name").isEqualTo(accountFrom.name ?: "")
            .jsonPath("$.surname").isEqualTo(accountFrom.surname ?: "")
            .jsonPath("$.createDate").isEqualTo(accountFrom.createDate?.format(dtf) ?: "")
            .jsonPath("$.balance").isEqualTo(accountFrom.balance?.minus(amount) ?: "")


        //check other account balance
        webTestClient.get().uri("/account/${accountTo.id}").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo(accountTo.name ?: "")
            .jsonPath("$.surname").isEqualTo(accountTo.surname ?: "")
            .jsonPath("$.createDate").isEqualTo(accountTo.createDate?.format(dtf) ?: "")
            .jsonPath("$.lastUpdate").value(Matchers.greaterThan(accountTo.lastUpdate?.format(dtf) ?: ""))
            .jsonPath("$.balance").isEqualTo(accountTo.balance?.plus(amount) ?: "")
    }

    @Test
    fun shouldGetBadRequestWhenTransferBalance() {
        //given
        val accountFrom = repository.save(
            Account(
                name = "William",
                surname = "Shwarts",
                balance = 500.0871,
            )
        )
        val accountTo = repository.save(
            Account(
                name = "John",
                surname = "Smit",
                balance = 345.0321,
            )
        )
        val amount = 500.09
        //when
        //then
        webTestClient.post().uri("/account/transfer?from={accountId}&to={accountId}", accountFrom.id, accountTo.id)
            .body(Mono.just(amount)).exchange()
            .expectStatus().isBadRequest


        //check accounts balances not changed
        webTestClient.get().uri("/account/${accountFrom.id}").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo(accountFrom.name ?: "")
            .jsonPath("$.surname").isEqualTo(accountFrom.surname ?: "")
            .jsonPath("$.createDate").isEqualTo(accountFrom.createDate?.format(dtf) ?: "")
            .jsonPath("$.lastUpdate").isEqualTo(accountFrom.lastUpdate?.format(dtf) ?: "")
            .jsonPath("$.balance").isEqualTo(accountFrom.balance ?: "")

        webTestClient.get().uri("/account/${accountTo.id}").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo(accountTo.name ?: "")
            .jsonPath("$.surname").isEqualTo(accountTo.surname ?: "")
            .jsonPath("$.createDate").isEqualTo(accountTo.createDate?.format(dtf) ?: "")
            .jsonPath("$.lastUpdate").isEqualTo(accountTo.lastUpdate?.format(dtf) ?: "")
            .jsonPath("$.balance").isEqualTo(accountTo.balance ?: "")
    }
}
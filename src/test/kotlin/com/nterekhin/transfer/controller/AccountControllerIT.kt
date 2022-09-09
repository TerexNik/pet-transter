package com.nterekhin.transfer.controller

import com.nterekhin.transfer.FullContextTest
import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.entity.Account
import com.nterekhin.transfer.repository.AccountRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import org.springframework.util.ResourceUtils
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@AutoConfigureWebTestClient
class AccountControllerIT : FullContextTest() {

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
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.name").isEqualTo("William")
            .jsonPath("$.surname").isEqualTo("Shwarts")
            .jsonPath("$.balance").isEqualTo(120500.3971)
    }
}
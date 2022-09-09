package com.nterekhin.transfer.controller

import com.nterekhin.transfer.FullContextTest
import com.nterekhin.transfer.model.dto.AccountDTO
import com.nterekhin.transfer.model.entity.Account
import com.nterekhin.transfer.repository.AccountRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.util.ResourceUtils
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import javax.persistence.Column

@AutoConfigureWebTestClient
class AccountControllerIT : FullContextTest() {

    @Autowired
    lateinit var repository: AccountRepository

    @Autowired
    lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun init() {
        val account = Account(
            id = 1,
            name = "John",
            surname = "Smit",
            createDate = LocalDateTime.of(2022, 9, 9, 19, 30, 5),
            lastUpdate = LocalDateTime.of(2022, 9, 9, 19, 30, 5),
            balance = 345.0321,
        )
        repository.save(account)
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }


    @Test
    fun shouldGetAccountDetails() {
        val validAccountJson = ResourceUtils.getFile("classpath:json/account.json")

        webTestClient.get().uri("/account/1").exchange()
            .expectStatus().isOk
            .expectBody()
            .json(validAccountJson.readText())
    }

    @Test
    fun shouldGetNoContent() {
        webTestClient.get().uri("/account/2").exchange()
            .expectStatus().isNoContent
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
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.name").isEqualTo("William")
            .jsonPath("$.surname").isEqualTo("Shwarts")
            .jsonPath("$.balance").isEqualTo(120500.3971)
    }
}
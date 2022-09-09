package com.nterekhin.transfer.controller

import com.nterekhin.transfer.FullContextTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.ResourceUtils

@AutoConfigureWebTestClient
class TransferControllerIT : FullContextTest() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun shouldGetAccountDetails() {
        val validAccountJson = ResourceUtils.getFile("classpath:json/account.json")

        webTestClient.get().uri("/account").exchange()
            .expectStatus().isOk
            .expectBody()
            .json(validAccountJson.readText())
    }
}
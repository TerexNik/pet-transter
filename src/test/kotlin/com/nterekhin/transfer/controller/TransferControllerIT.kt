package com.nterekhin.transfer.controller

import com.nterekhin.transfer.FullContextTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient

@AutoConfigureWebTestClient
class TransferControllerIT : FullContextTest() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun shouldGetAccountDetails() {
        webTestClient.get().uri("/account").exchange()
            .expectStatus().isOk
    }
}
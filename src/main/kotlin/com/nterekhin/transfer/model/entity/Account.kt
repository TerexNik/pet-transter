package com.nterekhin.transfer.model.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "account")
data class Account(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column
    val name: String? = null,
    @Column
    val surname: String? = null,
    @Column
    val createDate: LocalDateTime? = null,
    @Column
    val lastUpdate: LocalDateTime? = null,
    @Column
    val balance: Double? = null,
)

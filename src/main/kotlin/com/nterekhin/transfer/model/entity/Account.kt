package com.nterekhin.transfer.model.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account")
data class Account(
    @Id
    @Column(nullable = false, unique = true)
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

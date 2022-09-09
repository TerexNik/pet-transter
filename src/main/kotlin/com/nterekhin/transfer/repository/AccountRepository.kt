package com.nterekhin.transfer.repository

import com.nterekhin.transfer.model.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Int>
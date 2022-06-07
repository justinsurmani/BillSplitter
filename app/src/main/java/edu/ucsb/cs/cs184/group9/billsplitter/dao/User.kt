package edu.ucsb.cs.cs184.group9.billsplitter.dao

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val email: String? = null
)
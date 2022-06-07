package edu.ucsb.cs.cs184.group9.billsplitter.dao

import java.util.UUID

data class User(
    val id: String,
    val name: String?,
    val email: String?
)
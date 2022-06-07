package edu.ucsb.cs.cs184.group9.billsplitter.dao

data class Group(
    val owner: User? = null,
    val users : List<User> = listOf()
)
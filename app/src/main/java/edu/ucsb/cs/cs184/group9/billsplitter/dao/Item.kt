package edu.ucsb.cs.cs184.group9.billsplitter.dao

data class Item(
    val name : String,
    val price: Int,
    val payers: List<User>
)
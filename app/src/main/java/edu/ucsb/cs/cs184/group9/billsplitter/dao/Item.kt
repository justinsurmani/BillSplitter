package edu.ucsb.cs.cs184.group9.billsplitter.dao

data class Item(
    val name : String? = null,
    val price: Int = 0,
    val payers: Map<String, Boolean> = mapOf()
)
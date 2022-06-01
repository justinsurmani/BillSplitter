package edu.ucsb.cs.cs184.group9.billsplitter.dao

import java.util.UUID

data class Bill(
    val id: String,
    val total: Int,
    val items : List<Item> = listOf()
) {
    val currentTotal = items.sumOf { it.price }
    val remainingTotal = total - currentTotal
}
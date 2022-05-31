package edu.ucsb.cs.cs184.group9.billsplitter.dao

data class Bill(
    val group: Group,
    val total: Int,
    val items : List<Item> = listOf()
) {
    val currentTotal = items.sumOf { it.price }
    val remainingTotal = total - currentTotal
}
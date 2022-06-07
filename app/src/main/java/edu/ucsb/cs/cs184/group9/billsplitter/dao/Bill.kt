package edu.ucsb.cs.cs184.group9.billsplitter.dao

import java.util.UUID

data class Bill(
    val id: String = UUID.randomUUID().toString(),
    val subtotal: Int = 0,
    val tax: Int = 0,
    val tip: Int = 0,
    val group: Group? = null,
    val items : List<Item> = listOf()
) {
    val total : Int = subtotal + tax + tip
    val currentTotalForItems : Int = items.sumOf { it.price }
    val totalsForEachUser : Map<String, Int> = run {
        val totalFor: MutableMap<String, Int> = mutableMapOf()

        group?.users?.forEach { totalFor[it.id] = 0 }

        items.forEach { item ->
            item.payers.entries.forEach {
                totalFor[it.key] = totalFor[it.key]!! + it.value
            }
        }

        totalFor.toMap()
    }
}
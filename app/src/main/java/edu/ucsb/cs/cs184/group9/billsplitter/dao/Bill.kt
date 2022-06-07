package edu.ucsb.cs.cs184.group9.billsplitter.dao

import com.google.firebase.firestore.Exclude
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
    val currentTotal : Int = items.sumOf { it.price }
    val remainingTotal : Int = total - currentTotal
    val totalsForEachUser : Map<String, Int> = run {
        val totalFor: MutableMap<String, Int> = mutableMapOf()

        group?.users?.forEach { totalFor[it.id!!] = 0 }

        items.forEach { item ->
            if (item.payers.isEmpty()) return@forEach

            var remainingTotalForItem = item.price
            val totalPerPayer = item.price / item.payers.size
            item.payers.forEachIndexed { i, payer ->
                val totalToAdd = if (i == item.payers.size - 1) remainingTotalForItem else totalPerPayer
                totalFor[payer.id!!] = totalFor[payer.id]!! + totalToAdd
                remainingTotalForItem -= totalToAdd
            }
        }

        totalFor.toMap()
    }
}
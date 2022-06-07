package edu.ucsb.cs.cs184.group9.billsplitter.dao

data class Bill(
    val id: String,
    val subtotal: Int,
    val tax: Int,
    val tip: Int,
    val group: Group,
    val items : List<Item> = listOf()
) {
    val total : Int = subtotal + tax + tip
    val currentTotal : Int = items.sumOf { it.price }
    val remainingTotal : Int = total - currentTotal
    val totalsForEachUser : Map<User, Int> = run {
        val totalFor: MutableMap<User, Int> = mutableMapOf()

        group.users.forEach { totalFor[it] = 0 }

        items.forEach { item ->
            if (item.payers.isEmpty()) return@forEach

            var remainingTotalForItem = item.price
            val totalPerPayer = item.price / item.payers.size
            item.payers.forEachIndexed { i, payer ->
                val totalToAdd = if (i == item.payers.size - 1) remainingTotalForItem else totalPerPayer
                totalFor[payer] = totalFor[payer]!! + totalToAdd
                remainingTotalForItem -= totalToAdd
            }
        }

        totalFor.toMap()
    }
}
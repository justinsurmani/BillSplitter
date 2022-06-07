package edu.ucsb.cs.cs184.group9.billsplitter.dao

data class Item(
    val name : String? = null,
    val price: Int = 0,
    val payers: Map<String, Int> = mapOf()
) {

    fun splitEvenly() : Item {
        return splitProportionally(this.payers.mapValues { 1.0 / this.payers.size })
    }

    fun splitProportionally(proportions: Map<String, Double>) : Item {
        val newPayers = this.payers.toMutableMap()
        var currentTotal : Int = 0
        this.payers.keys.forEachIndexed { i, id ->
            val toAdd = (proportions[id]!! * this.price).toInt()
            newPayers[id] = toAdd
            currentTotal += toAdd
            // if last one, add the remaining amount
            if (i == this.payers.size - 1) newPayers[id] = newPayers[id]!! + this.price - currentTotal
        }

        return this.copy(payers = newPayers)
    }
}
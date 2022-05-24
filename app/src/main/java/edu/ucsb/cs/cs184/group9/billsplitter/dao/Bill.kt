package edu.ucsb.cs.cs184.group9.billsplitter.dao

class Bill(
    val group: Group,
    val total: Int,
    private val items : MutableList<Item> = mutableListOf()
) {

    var currentTotal : Int = items.sumOf { it.price }
        private set
    val remainingTotal get() = total - currentTotal
    val isTotalMet get() = remainingTotal <= 0

    fun addItem(item : Item) {
        items.add(item)
        currentTotal += item.price
    }

    fun removeItem(item : Item) {
        if (items.remove(item)) {
            // successfully removed item
            currentTotal -= item.price
        }
    }
}
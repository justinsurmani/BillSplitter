package edu.ucsb.cs.cs184.group9.billsplitter.ui.util

import com.google.firebase.auth.FirebaseUser
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User

// helpful extension functions

internal fun FirebaseUser.asUser() : User {
    return User(this.uid, this.displayName ?: this.email!!, this.email!!)
}

internal fun String.asMoneyValue(): Int {
    val splatted = this.filter { it.isDigit() || it == '.' }
        .split(".", limit = 2)

    if (splatted.size == 1) {
        return splatted[0].toInt() * 100
    }

    val dollars = splatted[0].ifBlank { "0" }.toInt()
    val cents = splatted[1].substring(0, 2).ifBlank { "0" }.toInt()

    return dollars * 100 + cents
}

internal fun Int.asMoneyDecimal(): String {
    val dollars = (this / 100).toString()
    val cents = (this % 100).toString().padStart(2, '0')

    return "$dollars.$cents"
}

internal fun Int.asMoneyDisplay(): String {
    return "$${asMoneyDecimal()}"
}

internal fun <K, V> Map<K, V>.copyAndSetPayer(setPayer: Boolean, item : Pair<K, V>): Map<K, V> {
    val newSet = this.toMutableMap()

    if (setPayer)
        newSet[item.first] = item.second
    else
        newSet.remove(item.first)

    return newSet.toMap()
}

internal fun <T> List<T>.copyAndReplace(prevItem: T, newItem: T): List<T> {
    return this.map { if (it === prevItem) newItem else it }.toList()
}

internal fun <T> List<T>.copyAndAdd(newItem: T) : List<T> {
    val newList = this.toMutableList()
    newList.add(newItem)

    return newList.toList()
}

internal fun <T> List<T>.copyAndRemove(prevItem: T): List<T> {
    return this.filter { it !== prevItem }
}

internal fun <T> List<T>.copyAndResize(
    size: Int,
    defaultSupplier: (Int) -> T
): List<T> {
    return (0 until size).map {
        if (it < this.size) this[it]
        else defaultSupplier(it)
    }.toList()
}
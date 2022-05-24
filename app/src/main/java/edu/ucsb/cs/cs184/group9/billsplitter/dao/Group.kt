package edu.ucsb.cs.cs184.group9.billsplitter.dao

class Group(
    val owner: User,
    private val users : MutableList<User> = mutableListOf(),
    private val bills : MutableList<Bill> = mutableListOf()
) {

    fun addUser(user: User) {
        users.add(user)
    }

    fun addBill(bill: Bill) {
        bills.add(bill)
    }
}
package edu.ucsb.cs.cs184.group9.billsplitter.dao

class Item(
    val name : String,
    val price: Int,
    var payer: User?
)
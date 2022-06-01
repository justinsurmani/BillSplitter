package edu.ucsb.cs.cs184.group9.billsplitter.repository

import android.util.Log
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import java.util.UUID

object BillRepository {

    // temporary in memory "database" using a mutable map
    @JvmStatic
    private val db : MutableMap<String, Bill> = mutableMapOf()

    fun createBill(bill: Bill) {
        Log.i(this.javaClass.name, "Creating Bill $bill")

        db[bill.id] = bill
    }

    fun loadBill(id: String) : Bill? {
        Log.i(this.javaClass.name, "Load Bill $id")

        return db[id]
    }
}
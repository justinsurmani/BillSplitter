package edu.ucsb.cs.cs184.group9.billsplitter.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import java.util.UUID
import kotlinx.coroutines.tasks.await

object BillRepository {

//    // temporary in memory "database" using a mutable map
//    @JvmStatic
//    private val db : MutableMap<String, Bill> = mutableMapOf()

    private val db = Firebase.firestore

    fun createBill(bill: Bill) {
        Log.i(this.javaClass.name, "Creating Bill $bill")

        db.collection("bills")
            .document(bill.id)
            .set(bill)
    }

    suspend fun loadBill(id: String) : Bill? {
        Log.i(this.javaClass.name, "Load Bill $id")

        return db.collection("bills")
            .document(id)
            .get()
            .await()
            .toObject<Bill>()
    }
}
package edu.ucsb.cs.cs184.group9.billsplitter.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object BillRepository {

//    // temporary in memory "database" using a mutable map
//    @JvmStatic
//    private val db : MutableMap<String, Bill> = mutableMapOf()

    private val db = Firebase.firestore

    fun saveBill(bill: Bill?) {
        if (bill == null) return
        Log.i(this.javaClass.name, "Creating Bill $bill")

        db.collection("bills")
            .document(bill.id)
            .set(bill)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadBill(id: String) : Flow<Bill?> {
        Log.i(this.javaClass.name, "Load Bill $id")

        return callbackFlow {
            val snapshotListener = db.collection("bills")
                .document(id)
                .addSnapshotListener { qs, _ ->
                    trySend(qs?.toObject<Bill>())
                }

            awaitClose {
                snapshotListener.remove()
            }
        }
    }
}
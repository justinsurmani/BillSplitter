package edu.ucsb.cs.cs184.group9.billsplitter.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserRepository {
    private val _currentUser = MutableStateFlow(Firebase.auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    fun updateCurrentUser() {
        _currentUser.value = Firebase.auth.currentUser
    }
}
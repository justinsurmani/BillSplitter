package edu.ucsb.cs.cs184.group9.billsplitter.repository

import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserRepository {
    private val _currentUser : MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser = _currentUser.asStateFlow()

    fun updateCurrentUser(user: User?) {
        _currentUser.value = user
    }
}
package edu.ucsb.cs.cs184.group9.billsplitter.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import edu.ucsb.cs.cs184.group9.billsplitter.repository.UserRepository

@Composable
fun AuthenticatedRoute(
    toSignIn: () -> Unit,
    content: @Composable () -> Unit
) {
    val currentUser by UserRepository.currentUser.collectAsState()

    if (currentUser != null) {
        content()
    } else LaunchedEffect(true) {
        toSignIn()
    }
}
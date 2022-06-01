package edu.ucsb.cs.cs184.group9.billsplitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.ucsb.cs.cs184.group9.billsplitter.ui.bill.BillScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup.CreateGroupScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.BottomBar
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_BILL
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_HOME
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_PROFILE
import edu.ucsb.cs.cs184.group9.billsplitter.ui.profile.ProfileScreen
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
    
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = NAV_HOME
            ) {
                composable(NAV_HOME) { CreateGroupScreen(navController = navController) }
                composable(NAV_PROFILE) { ProfileScreen() }
                composable(
                    NAV_BILL,
                    arguments = listOf(navArgument("billId") { type = NavType.StringType })
                ) {
                    BillScreen(
                        navController = navController,
                        billId = it.arguments?.getString("billId")!!
                    )
                }
            }
        }
    }
}
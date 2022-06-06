package edu.ucsb.cs.cs184.group9.billsplitter.ui.nav

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.ucsb.cs.cs184.group9.billsplitter.MainActivity
import edu.ucsb.cs.cs184.group9.billsplitter.ui.bill.BillScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup.CreateGroupScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.login.LoginPage
import edu.ucsb.cs.cs184.group9.billsplitter.ui.login.RegisterPage
import edu.ucsb.cs.cs184.group9.billsplitter.ui.profile.ProfileScreen

@Composable
fun SetupNavGraph(navController: NavHostController, context: ComponentActivity) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginPage(context = context)
        }
        composable(route = Screen.Register.route) {
            RegisterPage(context = context)
        }
    }
}
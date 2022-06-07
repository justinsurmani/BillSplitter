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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.group9.billsplitter.ui.bill.BillScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup.CreateGroupScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.editprofile.EditProfileScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.login.LoginPage
import edu.ucsb.cs.cs184.group9.billsplitter.ui.login.RegisterPage
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.*
import edu.ucsb.cs.cs184.group9.billsplitter.ui.profile.ProfileScreen

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
                composable(NAV_HOME) {
                    AuthenticatedRoute(
                        toSignIn = {
                            navController.navigate(NAV_LOGIN)
                        }
                    ) {
                        CreateGroupScreen(navController = navController)
                    }
                }
                composable(NAV_PROFILE) {
                    AuthenticatedRoute(
                        toSignIn = {
                            navController.navigate(NAV_LOGIN)
                        }
                    ) {
                        ProfileScreen(navController = navController)
                    }
                }
                composable(NAV_EDIT_PROFILE) {
                    AuthenticatedRoute(
                        toSignIn = {
                            navController.navigate(NAV_LOGIN)
                        }
                    ) {
                        EditProfileScreen(navController = navController)
                    }
                }
                composable(
                    NAV_BILL,
                    arguments = listOf(navArgument("billId") { type = NavType.StringType })
                ) {
                    BillScreen(
                        billId = it.arguments?.getString("billId")!!
                    )
                }
                composable(NAV_LOGIN) { LoginPage(navController = navController) }
                composable(NAV_REGISTER) { RegisterPage(navController = navController) }
            }
        }
    }
}
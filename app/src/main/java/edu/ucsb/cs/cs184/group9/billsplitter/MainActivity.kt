package edu.ucsb.cs.cs184.group9.billsplitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.group9.billsplitter.ui.bill.BillScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup.CreateGroupScreen
import edu.ucsb.cs.cs184.group9.billsplitter.ui.login.LoginPage
import edu.ucsb.cs.cs184.group9.billsplitter.ui.login.RegisterPage
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.*
import edu.ucsb.cs.cs184.group9.billsplitter.ui.profile.ProfileScreen
import java.util.UUID

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        setContent {
            MainScreen()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser == null){
            setContent {
                navController = rememberNavController()
                SetupNavGraph(navController = navController, context = this)
//                LoginPage(this)
            }
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
                        billId = it.arguments?.getString("billId")!!
                    )
                }
//                composable(route = Screen.Login.route) {
//                    LoginPage(context = this@MainActivity)
//                }
//                composable(route = Screen.Register.route) {
//                    RegisterPage(context = this@MainActivity)
//                }
            }
        }
    }
}
package edu.ucsb.cs.cs184.group9.billsplitter.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ucsb.cs.cs184.group9.billsplitter.repository.UserRepository
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_HOME
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_REGISTER
import edu.ucsb.cs.cs184.group9.billsplitter.ui.theme.primaryColor
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginPageViewModel: ViewModel() {
    fun signIn(
        email: String,
        password: String,
        onSuccess: (AuthResult) -> Unit
    ) = viewModelScope.launch {
        try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Log.i(javaClass.name, "Login successful ${result.user}")
            onSuccess(result)
        } catch (e: Exception) {
            Log.e(javaClass.name, "Login failed ${e.message}")
        }
    }
}

@Composable
fun LoginPage(
    navController: NavController,
    loginPageViewModel: LoginPageViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Login",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )

        var email by remember { mutableStateOf("") }
        TextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            label = { Text("Email") }
        )

        var password by rememberSaveable { mutableStateOf("") }
        var passwordHidden by rememberSaveable { mutableStateOf(true) }
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )


        Button(
            onClick = {
                loginPageViewModel.signIn(email, password) {
                    UserRepository.updateCurrentUser()
                    navController.navigate(NAV_HOME)
                }
            }
        ) {
            Text("Login")
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                text = "Not registered yet? ",
                fontSize = 16.sp,
            )
            TextButton(onClick = {
                navController.navigate(NAV_REGISTER)
            })
            {
                Text("Register here")
            }
        }

    }
}

@Preview
@Composable
private fun LoginPagePreview() {
    val navController = rememberNavController()
    LoginPage(navController = navController)
}
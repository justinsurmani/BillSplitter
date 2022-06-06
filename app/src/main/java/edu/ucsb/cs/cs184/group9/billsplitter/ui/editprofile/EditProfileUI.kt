package edu.ucsb.cs.cs184.group9.billsplitter.ui.editprofile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import edu.ucsb.cs.cs184.group9.billsplitter.R
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_BILL
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_PROFILE
import org.intellij.lang.annotations.JdkConstants

class ProfileViewModel : ViewModel() {
    private val _name : MutableLiveData<String> = MutableLiveData("test")
    val name : LiveData<String> = _name

    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

@Composable
fun EditProfileScreen (navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {

    val notification = rememberSaveable{ mutableStateOf("") }
    if(notification.value.isNotEmpty())
    {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var name by rememberSaveable { mutableStateOf("name")}

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )

        {
            Text(
                text = "Cancel",
                fontSize = 16.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable
                {
                    notification.value = "Cancelled"
                    navController.navigate(NAV_PROFILE)
                }
            )
            Text(
                text = "Done",
                fontSize = 16.sp,
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable
                {
                    notification.value = "Profile updated"
                    navController.navigate(NAV_PROFILE)
                }
            )
        }
        EditProfilePicture()

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(text = "Name:",
                fontSize = 16.sp,
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black
                )
                )
        }
    }
}

@Composable
fun EditProfilePicture() {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.ic_defaultprofilepicture
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
    {
            uri: Uri? ->   uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        )
        {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Change profile picture",
            color = Color.Blue,
            fontSize = 16.sp
        )
        Text(
            text = "@User123",
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun EditProfileContent(name: String, onNameChange: (String) -> Unit) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "This is the profile page, $name!"
        )
    }
}
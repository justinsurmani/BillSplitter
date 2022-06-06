package edu.ucsb.cs.cs184.group9.billsplitter.ui.profile


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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import edu.ucsb.cs.cs184.group9.billsplitter.R
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_EDIT_PROFILE
import org.intellij.lang.annotations.JdkConstants

class ProfileViewModel : ViewModel() {
    private val _name : MutableLiveData<String> = MutableLiveData("test")
    val name : LiveData<String> = _name

    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

@Composable
fun ProfileScreen (navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {

    val notification = rememberSaveable{ mutableStateOf("") }
    if(notification.value.isNotEmpty())
    {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }
    
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
            horizontalArrangement = Arrangement.End
            )

            {
            Text(text = "Edit profile",
                modifier = Modifier
                .clickable { navController.navigate(NAV_EDIT_PROFILE) })
        }
        ProfilePicture()
    }
}

@Composable
fun ProfilePicture() {
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
                    .wrapContentSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Justin Surmani",
            fontSize = 20.sp
        )
        Divider(color = Color.Gray, thickness = 2.dp)
    }
    Row(
        modifier = Modifier
            //.padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center)
    {
        Text(
            text = "Bill History",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
fun ProfileContent(name: String, onNameChange: (String) -> Unit) {
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
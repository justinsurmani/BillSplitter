package edu.ucsb.cs.cs184.group9.billsplitter.ui.profile


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import edu.ucsb.cs.cs184.group9.billsplitter.R
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import edu.ucsb.cs.cs184.group9.billsplitter.repository.BillRepository
import edu.ucsb.cs.cs184.group9.billsplitter.repository.UserRepository
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_BILL
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_EDIT_PROFILE
import edu.ucsb.cs.cs184.group9.billsplitter.ui.theme.primaryColor
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyDisplay


class ProfileViewModel : ViewModel() {
    val user = UserRepository.currentUser.value!!
    val bills : LiveData<List<Bill?>> = BillRepository.loadBillsFor(user.email!!).asLiveData()
}


@Composable
fun ProfileScreen (
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val bills by profileViewModel.bills.observeAsState(listOf())
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
            //Text(text = "Edit profile",
              //  modifier = Modifier
                //.clickable { navController.navigate(NAV_EDIT_PROFILE) })

                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp)
                )
                {
                    Image(
                        painter = rememberImagePainter(R.drawable.ic_settings_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable { navController.navigate(NAV_EDIT_PROFILE) },
                        contentScale = ContentScale.Crop
                    )
                }
        }

        ProfilePicture(profileViewModel.user)
        BillHistory(
            bills = bills.requireNoNulls(),
            onBillNavigate = {
                navController.navigate(NAV_BILL.replace("{billId}", it.id))
            }
        )
    }
}

@Composable
fun ProfilePicture(user: User) {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painterProfilePic = rememberImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.ic_defaultprofilepicture
        else
            imageUri.value
    )
    
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
                painter = painterProfilePic,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = user.email!!,
            fontSize = 20.sp
        )
        Divider(color = primaryColor, thickness = 2.dp)
    }
}

@Composable
fun BillHistory(
    bills : List<Bill>,
    onBillNavigate : (Bill) -> Unit
) {
    Column(
        modifier = Modifier
            //.padding(8.dp)
            .fillMaxWidth()
    )
    {
        Text(
            text = "Bill History",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            ),
        )
        bills.forEach { bill ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onBillNavigate(bill)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = bill.id)
                Text(text = bill.total.asMoneyDisplay())
            }
        }
    }

}
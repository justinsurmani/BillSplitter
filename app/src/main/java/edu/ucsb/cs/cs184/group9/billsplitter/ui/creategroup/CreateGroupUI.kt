package edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Item
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import edu.ucsb.cs.cs184.group9.billsplitter.repository.BillRepository
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_BILL
import java.util.UUID

class CreateGroupViewModel : ViewModel() {
    private val _amountOfPeople : MutableLiveData<Int> = MutableLiveData(2)
    val amountOfPeople : LiveData<Int> = _amountOfPeople

    fun onAmountOfPeopleChange(newAmount: Int) {
        _amountOfPeople.value = newAmount
    }
}

@Composable
fun CreateGroupScreen(
    navController : NavController,
    createGroupViewModel: CreateGroupViewModel = viewModel()
) {
    val amountOfPeople by createGroupViewModel.amountOfPeople.observeAsState(2)

    CreateGroupContent(
        amountOfPeople = amountOfPeople,
        onAmountOfPeopleChange = { createGroupViewModel.onAmountOfPeopleChange(it) },
        onCreate = {
            val sampleBill = Bill(
                id = UUID.randomUUID().toString(),
                total = 10000,
                items = (1..amountOfPeople).map {
                    Item("$it's share", 0, User("$it", "User $it"))
                }
            )
            BillRepository.createBill(sampleBill)
            navController.navigate(NAV_BILL.replace("{billId}", sampleBill.id))
        }
    )
}

@Composable
fun CreateGroupContent(
    amountOfPeople : Int,
    onAmountOfPeopleChange : (Int) -> Unit,
    onCreate : () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Create a group"
        )

        DropdownNumberMenu(
            range = 2..8,
            selectedValue = amountOfPeople,
            onNumberSelected = onAmountOfPeopleChange
        )

        Text(
            text = "You've selected $amountOfPeople of people"
        )

        Button(
            onClick = onCreate
        ) {
            Text(text = "Create")
        }
    }
}

// a drop down menu component
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownNumberMenu(
    range: IntRange,
    selectedValue : Int,
    onNumberSelected : (Int) -> Unit
) {
    var expanded : Boolean by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selectedValue.toString(),
            onValueChange = { },
            label = { Text("Amount") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            range.forEach {
                DropdownMenuItem(
                    onClick = {
                        onNumberSelected(it)
                        expanded = false
                    }
                ) {
                    Text(text = it.toString())
                }
            }
        }
    }
}
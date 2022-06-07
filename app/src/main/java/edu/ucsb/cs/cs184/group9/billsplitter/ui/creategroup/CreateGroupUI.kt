package edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Group
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import edu.ucsb.cs.cs184.group9.billsplitter.repository.BillRepository
import edu.ucsb.cs.cs184.group9.billsplitter.repository.UserRepository
import edu.ucsb.cs.cs184.group9.billsplitter.ui.components.DropdownNumberMenu
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_BILL
import edu.ucsb.cs.cs184.group9.billsplitter.ui.theme.primaryColor
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyDecimal
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyDisplay
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyValue
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndResize
import java.util.UUID

class CreateGroupViewModel : ViewModel() {
    private val _group : MutableLiveData<Group> = MutableLiveData(
        Group(
            owner = UserRepository.currentUser.value!!,
            users = listOf(
                UserRepository.currentUser.value!!,
                User(name = "Mary")
            )
        )
    )
    private val _subtotal : MutableLiveData<Int> = MutableLiveData(0)
    private val _tax : MutableLiveData<Int> = MutableLiveData(0)
    private val _tip : MutableLiveData<Int> = MutableLiveData(0)
    val group : LiveData<Group> = _group
    val subtotal : LiveData<Int> = _subtotal
    val tax : LiveData<Int> = _tax
    val tip : LiveData<Int> = _tip

    fun onAmountOfPeopleChange(newAmount: Int) {
        val newUsers = _group.value?.users?.copyAndResize(newAmount) {
            User(name = "User $it")
        }
        _group.value = _group.value?.copy(
            users = newUsers.orEmpty()
        )
    }

    fun onSubtotalChange(newAmount: Int) {
        _subtotal.value = newAmount
    }

    fun onTaxChange(newAmount: Int) {
        _tax.value = newAmount
    }

    fun onTipChange(newAmount: Int) {
        _tip.value = newAmount
    }
}

@Composable
fun CreateGroupScreen(
    navController : NavController,
    createGroupViewModel: CreateGroupViewModel = viewModel()
) {
    val group by createGroupViewModel.group.observeAsState(Group(null))
    val subtotal by createGroupViewModel.subtotal.observeAsState(0)
    val tax by createGroupViewModel.tax.observeAsState(0)
    val tip by createGroupViewModel.tip.observeAsState(0)

    CreateGroupContent(
        group = group,
        subtotal = subtotal,
        tax = tax,
        tip = tip,
        onAmountOfPeopleChange = { createGroupViewModel.onAmountOfPeopleChange(it) },
        onSubtotalChange = { createGroupViewModel.onSubtotalChange(it) },
        onTaxChange = { createGroupViewModel.onTaxChange(it) },
        onTipChange = { createGroupViewModel.onTipChange(it) },
        onCreate = {
            val sampleBill = Bill(
                id = UUID.randomUUID().toString(),
                subtotal = subtotal,
                tax = tax,
                tip = tip,
                group = group
            )
            BillRepository.createBill(sampleBill)
            navController.navigate(NAV_BILL.replace("{billId}", sampleBill.id))
        }
    )
}

@Composable
fun CreateGroupContent(
    group: Group,
    subtotal: Int,
    tax: Int,
    tip: Int,
    onAmountOfPeopleChange : (Int) -> Unit,
    onSubtotalChange: (Int) -> Unit,
    onTaxChange: (Int) -> Unit,
    onTipChange: (Int) -> Unit,
    onCreate : () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var subtotalDisplay by rememberSaveable { mutableStateOf(subtotal.asMoneyDecimal()) }
    var taxDisplay by rememberSaveable { mutableStateOf(tax.asMoneyDecimal()) }
    var tipDisplay by rememberSaveable { mutableStateOf(tip.asMoneyDecimal()) }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Create a Group",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        DropdownNumberMenu(
            range = 2..8,
            selectedValue = group.users.size,
            onNumberSelected = onAmountOfPeopleChange
        )
        Text(
            text = "You've selected ${group.users.size} people"
        )
        MoneyNumberInput(
            label = "Subtotal",
            value = subtotalDisplay,
            onValueChange = { subtotalDisplay = it },
            onDone = {
                focusManager.clearFocus()
                subtotalDisplay = subtotalDisplay.asMoneyValue().asMoneyDecimal()
                onSubtotalChange(subtotalDisplay.asMoneyValue())
            }
        )
        MoneyNumberInput(
            label = "Tax",
            value = taxDisplay,
            onValueChange = { taxDisplay = it },
            onDone = {
                focusManager.clearFocus()
                taxDisplay = taxDisplay.asMoneyValue().asMoneyDecimal()
                onTaxChange(taxDisplay.asMoneyValue())
            }
        )
        MoneyNumberInput(
            label = "Tip",
            value = tipDisplay,
            onValueChange = { tipDisplay = it },
            onDone = {
                focusManager.clearFocus()
                tipDisplay = tipDisplay.asMoneyValue().asMoneyDecimal()
                onTipChange(tipDisplay.asMoneyValue())
            }
        )
        Button(
            onClick = onCreate
        ) {
            Text(text = "Create")
        }
    }
}

@Composable
fun MoneyNumberInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: KeyboardActionScope.() -> Unit
) {
    OutlinedTextField(
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Enter $label")},
        singleLine = true,
        leadingIcon = { Text(text = "$") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = onDone),
        modifier = Modifier.padding(16.dp)
    )
}

@Preview
@Composable
private fun CreateGroupUIPreview() {
    val navController = rememberNavController()
    UserRepository.updateCurrentUser(User(UUID.randomUUID().toString(), "Bob", "bob@bob.com"))
    CreateGroupScreen(navController = navController)
}
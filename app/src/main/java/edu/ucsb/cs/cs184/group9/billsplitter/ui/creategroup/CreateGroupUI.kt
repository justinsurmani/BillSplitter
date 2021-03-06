package edu.ucsb.cs.cs184.group9.billsplitter.ui.creategroup

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
import edu.ucsb.cs.cs184.group9.billsplitter.ui.components.ExpandableCard
import edu.ucsb.cs.cs184.group9.billsplitter.ui.nav.NAV_BILL
import edu.ucsb.cs.cs184.group9.billsplitter.ui.theme.primaryColor
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyDecimal
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyValue
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndReplace
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
    private val _name : MutableLiveData<String> = MutableLiveData()
    private val _subtotal : MutableLiveData<Int> = MutableLiveData(0)
    private val _tax : MutableLiveData<Int> = MutableLiveData(0)
    private val _tip : MutableLiveData<Int> = MutableLiveData(0)
    val group : LiveData<Group> = _group
    val name : LiveData<String> = _name
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

    fun onNameChange(newName: String) {
        _name.value = newName
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

    fun onGroupChange(newGroup: Group) {
        _group.value = newGroup
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
    val name by createGroupViewModel.name.observeAsState("")

    CreateGroupContent(
        group = group,
        subtotal = subtotal,
        tax = tax,
        tip = tip,
        name = name,
        onNameChange = { createGroupViewModel.onNameChange(it.orEmpty()) },
        onAmountOfPeopleChange = { createGroupViewModel.onAmountOfPeopleChange(it) },
        onSubtotalChange = { createGroupViewModel.onSubtotalChange(it) },
        onTaxChange = { createGroupViewModel.onTaxChange(it) },
        onTipChange = { createGroupViewModel.onTipChange(it) },
        onGroupChange = { createGroupViewModel.onGroupChange(it) },
        onCreate = {
            val sampleBill = Bill(
                id = UUID.randomUUID().toString(),
                name = name,
                subtotal = subtotal,
                tax = tax,
                tip = tip,
                group = group
            )
            BillRepository.saveBill(sampleBill)
            navController.navigate(NAV_BILL.replace("{billId}", sampleBill.id))
        }
    )
}

@Composable
fun CreateGroupContent(
    group: Group,
    name: String,
    subtotal: Int,
    tax: Int,
    tip: Int,
    onGroupChange : (Group) -> Unit,
    onNameChange : (String?) ->  Unit,
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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
        TextInput(
            label = "Bill Name",
            value = name,
            onDone = {
                focusManager.clearFocus()
                onNameChange(it)
            }
        )
        DropdownNumberMenu(
            range = 2..8,
            selectedValue = group.users.size,
            onNumberSelected = onAmountOfPeopleChange
        )
        ExpandableCard(
            modifier = Modifier.padding(10.dp),
            title = {
                Text(text = "Add/Edit Users")
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                group.users.forEach { prev ->
                    TextInput(
                        label = "Edit Name",
                        value = prev.name.orEmpty(),
                        onDone = { value ->
                            focusManager.clearFocus()
                            val newUser = prev.copy(
                                name = value,
                                email = if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) value else ""
                            )
                            onGroupChange(group.copy(
                                users = group.users.copyAndReplace(prev, newUser)
                            ))
                        }
                    )
                }
            }
        }
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
fun TextInput(
    label: String,
    value: String,
    onDone: (String) -> Unit
) {
    var valueDisplay by rememberSaveable { mutableStateOf(value) }
    OutlinedTextField(
        label = { Text(text = label) },
        value = valueDisplay,
        onValueChange = { valueDisplay = it },
        placeholder = { Text(text = "Enter $label")},
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onDone(valueDisplay)
        }),
        modifier = Modifier.padding(10.dp)
    )
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
        modifier = Modifier.padding(10.dp)
    )
}

@Preview
@Composable
private fun CreateGroupUIPreview() {
    val navController = rememberNavController()
    UserRepository.updateCurrentUser(User(UUID.randomUUID().toString(), "Bob", "bob@bob.com"))
    CreateGroupScreen(navController = navController)
}
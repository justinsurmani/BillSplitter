package edu.ucsb.cs.cs184.group9.billsplitter.ui.bill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Group
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Item
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import edu.ucsb.cs.cs184.group9.billsplitter.repository.BillRepository
import edu.ucsb.cs.cs184.group9.billsplitter.ui.components.ExpandableCard
import edu.ucsb.cs.cs184.group9.billsplitter.ui.components.MultiSelectBox
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyDisplay
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyValue
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAnd
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndAdd
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndReplace
import java.util.UUID

class BillViewModelFactory(private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(Bill::class.java)
            .newInstance(BillRepository.loadBill(id))
}

class BillViewModel(bill: Bill) : ViewModel() {
    private val _bill : MutableLiveData<Bill> = MutableLiveData(bill)
    private val _tip : MutableLiveData<Int> = MutableLiveData(15)
    val bill : LiveData<Bill> = _bill
    val tip : LiveData<Int> = _tip

    fun onTipChange(newTip: Int) {
        _tip.value = newTip
    }

    fun onBillChange(newBill: Bill) {
        _bill.value = newBill
    }

    fun onItemChange(item: Item, newItem: Item) {
        val newItems = bill.value?.items?.copyAndReplace(item, newItem)
        val newBill = bill.value?.copy(items = newItems.orEmpty())
        _bill.value = newBill
    }
}

@Composable
fun BillScreen(
    billId : String,
    billViewModel: BillViewModel = viewModel(factory = BillViewModelFactory(billId))
) {
    val bill by billViewModel.bill.observeAsState()
    val tip by billViewModel.tip.observeAsState()

    BillContent(
        bill = bill!!,
        tip = tip!!,
        onBillChange = { billViewModel.onBillChange(it) },
        onItemChange = { prev, new -> billViewModel.onItemChange(prev, new) },
        onTipSelected = { billViewModel.onTipChange(it) }
    )
}

@Composable
private fun BillContent(
    bill : Bill,
    tip : Int,
    onBillChange: (newBill: Bill) -> Unit,
    onItemChange: (prev: Item, new: Item) -> Unit,
    onTipSelected: (Int) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "Your Bill"
        )
        Text(
            text = "Bill Total: ${bill.total.asMoneyDisplay()}"
        )
        Text(
            text = "${bill.remainingTotal.asMoneyDisplay()} left"
        )
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            bill.items.forEach {
                BillItem(
                    bill = bill,
                    billItem = it,
                    onItemChange = onItemChange
                )
            }
            Button(
                onClick = {
                    val newBill = bill.copy(items = bill.items.copyAndAdd(Item("New Item", 0)))
                    onBillChange(newBill)
                }
            ) {
                Text(text = "Add Item")
            }
        }

        Text(text = "$tip%")
        TipSlider(onTipSelected = onTipSelected)

        Text(text = "Totals for each User")
        bill.group.users.forEach { user ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = user.name)
                Text(text = bill.totalsForEachUser[user]!!.asMoneyDisplay())
            }
        }

        Text(
            text = "Current total: ${bill.currentTotal.asMoneyDisplay()}"
        )
    }
}

@Composable
private fun BillItem(
    bill : Bill,
    billItem : Item,
    onItemChange: (prev: Item, new: Item) -> Unit
) {
    var priceDisplay by remember { mutableStateOf(billItem.price.asMoneyDisplay()) }
    var nameDisplay by remember { mutableStateOf(billItem.name) }
    val focusManager = LocalFocusManager.current

    ExpandableCard(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = billItem.name)
                Text(text = billItem.price.asMoneyDisplay())
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                label = { Text(text = "Item Name") },
                value = nameDisplay,
                onValueChange = { value ->
                    nameDisplay = value
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    onItemChange(billItem, billItem.copy(name = nameDisplay))
                    focusManager.clearFocus()
                })
            )
            OutlinedTextField(
                label = { Text(text="Price") },
                value = priceDisplay,
                onValueChange = { value ->
                    priceDisplay = value
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    onItemChange(billItem, billItem.copy(price = priceDisplay.asMoneyValue()))
                    // change local state
                    priceDisplay = priceDisplay.asMoneyValue().asMoneyDisplay()
                    focusManager.clearFocus()
                }),
            )
        }
        Text(text = "Payer(s)")
        MultiSelectBox(
            items = bill.group.users.map { it to (it in billItem.payers) },
            stringifyItem = User::name,
            onItemChange = {
                val addToSet = it.second
                onItemChange(billItem, billItem.copy(payers = billItem.payers.copyAnd(addToSet, it.first)))
            }
        )
    }
}

@Composable
private fun TipSlider(onTipSelected: (Int) -> Unit, tipValues: List<Int> = listOf(0, 15, 20, 25)) {
    var sliderPosition by remember { mutableStateOf(1f) }
    Text(text = "Tip")
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..(tipValues.size - 1).toFloat(),
        onValueChangeFinished = {
            onTipSelected(tipValues[sliderPosition.toInt()])
        },
        steps = tipValues.size - 2
    )
}

// preview composable
@Preview
@Composable
private fun BillUIPreview() {
    val users = (1..4).map { User(UUID.randomUUID().toString(), "User $it", "user@user.com") }
    val sampleGroup = Group(users[0], users)
    val sampleBill = Bill(
        id = UUID.randomUUID().toString(),
        total = 10000,
        group = sampleGroup,
        items = listOf(Item("Sample Item", 0))
    )
    BillRepository.createBill(sampleBill)
    BillScreen(billId = sampleBill.id)
}


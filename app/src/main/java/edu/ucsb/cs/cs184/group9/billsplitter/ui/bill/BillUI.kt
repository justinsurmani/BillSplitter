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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.ucsb.cs.cs184.group9.billsplitter.R
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Group
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Item
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import edu.ucsb.cs.cs184.group9.billsplitter.repository.BillRepository
import edu.ucsb.cs.cs184.group9.billsplitter.ui.components.ExpandableCard
import edu.ucsb.cs.cs184.group9.billsplitter.ui.components.MultiSelectBox
import edu.ucsb.cs.cs184.group9.billsplitter.ui.theme.primaryColor
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyDisplay
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.asMoneyValue
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndAdd
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndRemove
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndReplace
import edu.ucsb.cs.cs184.group9.billsplitter.ui.util.copyAndSetPayer
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.UUID

class BillViewModelFactory(private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(String::class.java)
            .newInstance(id)
}

class BillViewModel(id: String) : ViewModel() {
    private val _tip : MutableLiveData<Int> = MutableLiveData(15)
    val bill : LiveData<Bill?> = BillRepository.loadBill(id).asLiveData()

    fun onBillChange(newBill: Bill) {
        BillRepository.saveBill(newBill)
    }
}

@Composable
fun BillScreen(
    billId : String,
    billViewModel: BillViewModel = viewModel(factory = BillViewModelFactory(billId))
) {
    val bill by billViewModel.bill.observeAsState()

    if (bill == null) { return }

    BillContent(
        bill = bill!!,
        onBillChange = { billViewModel.onBillChange(it) }
    )
}

@Composable
private fun BillContent(
    bill : Bill,
    onBillChange: (newBill: Bill) -> Unit
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
            text = bill.name ?: "Your Bill",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Bill Subtotal: ${bill.subtotal.asMoneyDisplay()}"
        )
        Text(
            text = "${(max(bill.subtotal - bill.currentTotalForItems, 0)).asMoneyDisplay()} left for subtotal"
        )
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            bill.items.forEach {
                BillItem(
                    bill = bill,
                    billItem = it,
                    onItemChange = { prev, new ->
                        onBillChange(bill.copy(items = bill.items.copyAndReplace(prev, new)))
                    },
                    onItemDelete = { item ->
                        onBillChange(bill.copy(items = bill.items.copyAndRemove(item)))
                    }
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val newBill = bill.copy(items = bill.items.copyAndAdd(Item("New Item", 0)))
                    onBillChange(newBill)
                }
            ) {
                Text(text = "Add Item")
            }
        }
        Text(text = "Totals for each User")
        bill.group?.users?.forEach { user ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = user.name.orEmpty())
                Text(text = bill.totalsForEachUser[user.id]!!.asMoneyDisplay())
            }
        }
        SplitBillItem(
            label = "Tax",
            value = bill.tax,
            bill = bill,
            onBillChange = onBillChange
        )
        SplitBillItem(
            label = "Tip",
            value = bill.tip,
            bill = bill,
            onBillChange = onBillChange
        )
        Text(
            text = "Current total: ${bill.totalsForEachUser.values.sum().asMoneyDisplay()}"
        )
        Text(
            text = "Bill Grand Total: ${bill.total.asMoneyDisplay()}"
        )
    }
}

@Composable
private fun BillItem(
    bill : Bill,
    billItem : Item,
    onItemChange: (prev: Item, new: Item) -> Unit,
    onItemDelete: (prev: Item) -> Unit
) {
    var priceDisplay by remember { mutableStateOf(billItem.price.asMoneyDisplay()) }
    var nameDisplay by remember { mutableStateOf(billItem.name) }
    val focusManager = LocalFocusManager.current

    ExpandableCard(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = billItem.name.orEmpty())
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
                value = nameDisplay.orEmpty(),
                singleLine = true,
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
                singleLine = true,
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
            items = bill.group?.users?.map {
                it to (it.id in billItem.payers.keys)
            }.orEmpty(),
            stringifyItem = { user ->
                "${user.name} ${(billItem.payers[user.id]?.asMoneyDisplay() ?: "")}"
            },
        ) {
            val id = it.first.id
            val addToSet = it.second
            val newItem = billItem.copy(
                payers = billItem.payers.copyAndSetPayer(addToSet, id to 0)
            ).splitEvenly()

            onItemChange(
                billItem,
                newItem
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            onClick = {
                onItemDelete(billItem)
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_delete_forever_24),
                    contentDescription = "Delete Item"
                )
            }
        )
    }
}

@Composable
fun SplitBillItem(
    label: String,
    value: Int,
    bill: Bill,
    onBillChange: (Bill) -> Unit
) {
    ExpandableCard(
        title = {
            Text(
                text = "$label: ${value.asMoneyDisplay()}"
            )
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val newItem = Item(
                        name = label,
                        price = value,
                        payers = bill.group?.users?.associateBy({ it.id }, { 0 }).orEmpty()
                    ).splitEvenly()

                    onBillChange(bill.copy(items = bill.items.copyAndAdd(newItem)))
                }
            ) {
                Text(text = "Split Evenly")
            }
            Button(
                onClick = {
                    val sum = bill.totalsForEachUser.values.sum().toDouble()
                    val newItem = Item(
                        name = label,
                        price = value,
                        payers = bill.group?.users?.associateBy({ it.id }, { 0 }).orEmpty()
                    ).splitProportionally(bill.totalsForEachUser.mapValues { it.value / sum })

                    onBillChange(bill.copy(items = bill.items.copyAndAdd(newItem)))
                }
            ) {
                Text(text = "Split Proportionally")
            }
        }
    }
}

// preview composable
@Preview
@Composable
private fun BillUIPreview() {
    val users = (1..4).map { User(UUID.randomUUID().toString(), "User $it", "user@user.com") }
    val sampleGroup = Group(users[0], users)
    val sampleBill = Bill(
        id = UUID.randomUUID().toString(),
        subtotal = 100_00,
        tax = 7_00,
        tip = 15_00,
        group = sampleGroup,
        items = listOf(Item("Sample Item", 0))
    )
    BillRepository.saveBill(sampleBill)
    BillScreen(billId = sampleBill.id)
}


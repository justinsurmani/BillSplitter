package edu.ucsb.cs.cs184.group9.billsplitter.ui.bill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Bill
import edu.ucsb.cs.cs184.group9.billsplitter.dao.Item
import edu.ucsb.cs.cs184.group9.billsplitter.repository.BillRepository
import java.util.UUID

class BillViewModelFactory(private val id: UUID) : ViewModelProvider.Factory {
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

    fun onItemChange(item: Item, newItem: Item) {
        val newItems = bill.value?.items?.map { if (it == item) newItem else it }
        val newBill = bill.value?.copy(items = newItems.orEmpty())
        _bill.value = newBill
    }
}

@Composable
fun BillScreen(
    navController : NavController,
    billViewModel: BillViewModel = viewModel(factory = BillViewModelFactory(UUID.randomUUID()))
) {
    val bill by billViewModel.bill.observeAsState()
    val tip by billViewModel.tip.observeAsState()

    BillContent(
        bill = bill!!,
        tip = tip!!,
        onItemChange = { prev, new -> billViewModel.onItemChange(prev, new) },
        onTipSelected = { billViewModel.onTipChange(it) }
    )
}

@Composable
private fun BillContent(
    bill : Bill,
    tip : Int,
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
            text = "${bill.remainingTotal.asMoneyDisplay()} left"
        )
        Column {
           bill.items.forEach {
               BillItem(
                   billItem = it,
                   onItemChange = onItemChange
               )
           }
        }
        Text(text = "$tip%")
        TipSlider(onTipSelected = onTipSelected)
        Text(
            text = "Your total: ${bill.total.asMoneyDisplay()}"
        )
    }
}

@Composable
private fun BillItem(
    billItem : Item,
    onItemChange: (prev: Item, new: Item) -> Unit
) {
    var priceDisplay by remember { mutableStateOf(billItem.price.asMoneyDisplay()) }
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
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
            modifier = Modifier.width(100.dp)
        )
        Text(text = "${billItem.payer.name}: ${billItem.price.asMoneyDisplay()}")
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

private fun String.asMoneyValue(): Int {
    val splatted = this.filter { it.isDigit() || it == '.' }
        .split(".", limit = 2)

    if (splatted.size == 1) {
        return splatted[0].toInt() * 100
    }

    val dollars = splatted[0].ifBlank { "0" }.toInt()
    val cents = splatted[1].substring(0, 2).ifBlank { "0" }.toInt()

    return dollars * 100 + cents
}

private fun Int.asMoneyDisplay(): String {
    val dollars = (this / 100).toString()
    val cents = (this % 100).toString().padStart(2, '0')
    return "$$dollars.$cents"
}



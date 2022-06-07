package edu.ucsb.cs.cs184.group9.billsplitter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import edu.ucsb.cs.cs184.group9.billsplitter.dao.User
import kotlin.reflect.KProperty1

@Composable
fun <T> MultiSelectBox(
    items: List<Pair<T, Boolean>>,
    stringifyItem: (T) -> String? = { it.toString() },
    onItemChange: (item: Pair<T, Boolean>) -> Unit
){
    Column {
        items.forEach { item ->
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = item.second,
                    onCheckedChange = {
                        onItemChange(item.copy(second = it))
                    }
                )
                Text(text = stringifyItem(item.first).orEmpty())
            }
        }
    }
}
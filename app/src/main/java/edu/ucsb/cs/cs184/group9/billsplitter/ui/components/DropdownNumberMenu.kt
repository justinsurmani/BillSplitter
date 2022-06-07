package edu.ucsb.cs.cs184.group9.billsplitter.ui.components

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
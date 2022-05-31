package edu.ucsb.cs.cs184.group9.billsplitter.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class HomeViewModel : ViewModel() {
    private val _name : MutableLiveData<String> = MutableLiveData("test")
    val name: LiveData<String> = _name;

    fun onNameChange(newName: String) {
        _name.value = newName
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val name : String by homeViewModel.name.observeAsState("test")
    HomeContent(name = name) { homeViewModel.onNameChange(it) }
}

@Composable
fun HomeContent(name: String, onNameChange : (String) -> Unit) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Hello, $name!"
        )
        Button (onClick = {
            val newName = if (name == "Updated") "test" else "Updated"
            onNameChange(newName)
        }) {
            Text(text = "Click me!")
        }
    }
}
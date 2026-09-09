package com.example.listycity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = {cityRepository.addCity(it)},
                        onRemoveCity = {cityRepository.removeCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}

class CityRepository{
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin",
        "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"
    )

    val cities: List<String> get() = _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun removeCity(index: Int)
    {
        _cities.removeAt(index)
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onRemoveCity: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    var newCityName by remember { mutableStateOf("") }
    var showInput by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1)}

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)){
            if(!showInput){
                Button(
                    onClick = {
                        showInput = true
                    }
                ) {
                    Text("Add City")
                }

                Spacer(modifier = Modifier.width(8.dp))

                if(!cities.isEmpty() && selectedIndex != -1){
                    Button(
                        onClick = {
                                onRemoveCity(selectedIndex)
                        }
                    ) {
                        Text("Remove City")
                    }
                }
            }

            if(showInput){
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = {newCityName = it},
                    label = {Text("City name")},
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (newCityName.isNotBlank()){
                            onAddCity(newCityName)
                            newCityName = ""
                            showInput = false
                        }
                    }
                ) {
                    Text("Confirm")
                }

                Spacer(modifier = Modifier.width(4.dp))

                Button(
                    onClick = {
                        if (newCityName.isNotBlank()){
                            newCityName = ""
                        }
                        showInput = false
                    }
                ) {
                    Text("Cancel")
                }
            }

        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            /*
            Looked up how to select an item in a LazyColumn
            Source - https://stackoverflow.com/a/72537011
            Posted by nglauber
            Retrieved 2026-09-09, License - CC BY-SA 4.0
            */
            items(cities.size){ index ->
                CityRow(
                    city = cities[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedIndex == index,
                            onClick = {selectedIndex = if (selectedIndex == index) -1 else index}
                        )
                        .background(
                            if(selectedIndex == index) Color.LightGray
                            else Color.Transparent
                        )
                )
            }
        }
    }
}

@Composable
fun CityRow(city: String, modifier: Modifier = Modifier){
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp)
    )
}
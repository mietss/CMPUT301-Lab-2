package com.example.listycity

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

//The list of cities
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



//The screen
@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onRemoveCity: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    var newCityName by remember { mutableStateOf("") }
    var showInput by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1)}

    //Inputs and controls
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)){

            if(!showInput){
                //Add city button
                Button(
                    onClick = {
                        showInput = true
                    }
                ) {
                    Text("Add City")
                }

                Spacer(modifier = Modifier.width(8.dp))

                //Remove city button,
                //only active when a city has been selected in the list, and the list is not empty
                if(!cities.isEmpty() && selectedIndex != -1){
                    Button(
                        onClick = {
                            onRemoveCity(selectedIndex)
                            selectedIndex = -1
                        }
                    ) {
                        Text("Remove City")
                    }
                }
            }

            //New city input
            if(showInput){
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = {newCityName = it},
                    label = {Text("City name")},
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                //Confirm button
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

                //Cancel button
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

        //City list
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
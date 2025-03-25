package com.example.logbook1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logbook1.ui.theme.Logbook1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Logbook1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LengthConverterScreen()
                }
            }
        }
    }
}

@Composable
fun LengthConverterScreen() {
    val units = listOf("Metre", "Millimetre", "Mile", "Foot")
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Metre") }
    var toUnit by remember { mutableStateOf("Metre") }
    var result by remember { mutableStateOf("Result will be shown here") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // State isShowExpanded dropdowns
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Length Unit Converter",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // text input (number keyboard)
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            isError = errorMessage != null
        )

        // display error message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Dropdown select from unit
        Text(
            text = "From Unit",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { fromExpanded = true }
                .padding(16.dp)
        ) {
            Text(text = fromUnit)
            DropdownMenu(
                expanded = fromExpanded,
                onDismissRequest = { fromExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            fromUnit = unit
                            fromExpanded = false
                        }
                    )
                }
            }
        }

        // Dropdown select to unit
        Text(
            text = "To Unit",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { toExpanded = true }
                .padding(16.dp)
        ) {
            Text(text = toUnit)
            DropdownMenu(
                expanded = toExpanded,
                onDismissRequest = { toExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            toUnit = unit
                            toExpanded = false
                        }
                    )
                }
            }
        }

        // Convert btn
        Button(
            onClick = {
                if (inputValue.isEmpty()) {
                    errorMessage = "Please enter a value"
                    return@Button
                }

                val value = inputValue.toDoubleOrNull()
                if (value == null || value < 0) {
                    errorMessage = "Please enter a valid positive number"
                    return@Button
                }

                errorMessage = null
                val convertedValue = convertLength(value, fromUnit, toUnit)
                result = "$value $fromUnit = $convertedValue $toUnit"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Convert")
        }

        // display result
        Text(
            text = result,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

private fun convertLength(value: Double, from: String, to: String): Double {
    // convert all value to meter as standard
    val valueInMetre = when (from) {
        "Metre" -> value
        "Millimetre" -> value / 1000
        "Mile" -> value / 0.000621371
        "Foot" -> value / 3.28084
        else -> value
    }

    // convert to unit to meter
    return when (to) {
        "Metre" -> valueInMetre
        "Millimetre" -> valueInMetre * 1000
        "Mile" -> valueInMetre * 0.000621371
        "Foot" -> valueInMetre * 3.28084
        else -> valueInMetre
    }
}
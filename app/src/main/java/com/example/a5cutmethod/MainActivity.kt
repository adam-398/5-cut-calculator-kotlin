package com.example.a5cutmethod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var nearSideWidth by remember { mutableStateOf("") }
            var farSideWidth by remember { mutableStateOf("") }
            var stripLength by remember { mutableStateOf("") }
            var fenceLength by remember { mutableStateOf("") }
            val result = calculateCut(nearSideWidth, farSideWidth, stripLength, fenceLength)
            var inches by remember { mutableStateOf("") }
            inchesToMetric(inches)

            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 75.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Woodworking helper",
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(
                        text = "5 cut calculator",
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    OutlinedTextField(
                        value = nearSideWidth,
                        onValueChange = { nearSideWidth = it },
                        label = { Text("Enter the width of the nearside:") },
                        modifier = Modifier
                            .padding(all = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    OutlinedTextField(
                        value = farSideWidth,
                        onValueChange = { farSideWidth = it },
                        label = { Text("Enter the width of the farside:") },
                        modifier = Modifier
                            .padding(all = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    OutlinedTextField(
                        value = stripLength,
                        onValueChange = { stripLength = it },
                        label = { Text("Enter the length of the strip:") },
                        modifier = Modifier
                            .padding(all = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    OutlinedTextField(
                        value = fenceLength,
                        onValueChange = { fenceLength = it },
                        label = { Text("Enter fence length:") },
                        modifier = Modifier
                            .padding(all = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Text(text = result)

                    Text(
                        text = "Imperial to metric converter",
                        modifier = Modifier.padding(top = 20.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    OutlinedTextField(
                        value = inches,
                        onValueChange = { inches = it },
                        label = { Text("Enter the length in inches:") },
                        modifier = Modifier
                            .padding(all = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Text(inchesToMetric(inches))
                }

            }

        }
    }
}


fun calculateCut(
    nearSideWidth: String,
    farSideWidth: String,
    stripLength: String,
    fenceLength: String
): String {
    val nearSideNum = nearSideWidth.toFloatOrNull()
    val farSideNum = farSideWidth.toFloatOrNull()
    val stripLengthNum = stripLength.toFloatOrNull()
    val fenceLengthNum = fenceLength.toFloatOrNull()

    if (nearSideNum == null || farSideNum == null || stripLengthNum == null || fenceLengthNum == null) {
        return "Please enter a valid input for all fields"
    }
    val result = ((farSideNum - nearSideNum) / 4) * (fenceLengthNum / stripLengthNum)
    if (result > 0) {
        return "the fence should be moved $result mm forwards"
    } else if (result < 0) {
        return "the fence should be moved $result mm backwards"
    } else {
        return "No cut needed, the widths are the same"
    }
}

fun inchesToMetric(inches: String): String {
    val inchesNum = inches.toFloatOrNull()

    if (inchesNum == null) {
        return "Please enter a valid input"
    } else {
        val inchToMilResult = "%.1f".format(inchesNum * 25.4)
        return "$inches inches is $inchToMilResult mm"
    }
}
package com.example.a5cutmethod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.a5cutmethod.ui.theme.FiveCutTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiveCutTheme {
                WoodworkingHelperScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoodworkingHelperScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Woodworking helper") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FiveCutCard()
            ConverterCard()
        }
    }
}

@Composable
fun FiveCutCard() {
    var nearSide by rememberSaveable { mutableStateOf("") }
    var farSide by rememberSaveable { mutableStateOf("") }
    var stripLength by rememberSaveable { mutableStateOf("") }
    var fenceLength by rememberSaveable { mutableStateOf("") }

    val adjustment = calculateCut(
        nearSide.toNumberOrNull(),
        farSide.toNumberOrNull(),
        stripLength.toNumberOrNull(),
        fenceLength.toNumberOrNull()
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Five-cut method", style = MaterialTheme.typography.titleLarge)
            Text(
                "Measure both ends of your test strip, then enter the values below.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MeasurementField("Near side", nearSide, { nearSide = it }, Modifier.weight(1f))
                MeasurementField("Far side", farSide, { farSide = it }, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MeasurementField("Strip length", stripLength, { stripLength = it }, Modifier.weight(1f))
                MeasurementField(
                    "Fence length",
                    fenceLength,
                    { fenceLength = it },
                    Modifier.weight(1f),
                    imeAction = ImeAction.Done
                )
            }

            ResultPanel(adjustment)
        }
    }
}

@Composable
fun ResultPanel(adjustment: Float?) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when {
                adjustment == null -> Text(
                    "Enter all four measurements to see the adjustment.",
                    style = MaterialTheme.typography.bodyMedium
                )

                abs(adjustment) < 0.005f -> {
                    Text("Square", style = MaterialTheme.typography.headlineMedium)
                    Text("No adjustment needed.", style = MaterialTheme.typography.bodyMedium)
                }

                else -> {
                    Text("Move the fence", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "%.2f mm".format(abs(adjustment)),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (adjustment > 0) "forwards" else "backwards",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ConverterCard() {
    var inches by rememberSaveable { mutableStateOf("") }
    val millimetres = inches.toNumberOrNull()?.times(25.4f)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Inches to millimetres", style = MaterialTheme.typography.titleLarge)
            MeasurementField(
                "Length",
                inches,
                { inches = it },
                Modifier.fillMaxWidth(),
                suffix = "in",
                imeAction = ImeAction.Done
            )
            Text(
                if (millimetres == null) "Enter a length in inches." else "%.1f mm".format(millimetres),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun MeasurementField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    suffix: String = "mm",
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        suffix = { Text(suffix) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction
        ),
        modifier = modifier
    )
}

fun calculateCut(
    nearSide: Float?,
    farSide: Float?,
    stripLength: Float?,
    fenceLength: Float?
): Float? {
    if (nearSide == null || farSide == null || stripLength == null || fenceLength == null) return null
    if (stripLength <= 0f) return null
    return ((farSide - nearSide) / 4f) * (fenceLength / stripLength)
}

fun String.toNumberOrNull(): Float? = trim().replace(',', '.').toFloatOrNull()
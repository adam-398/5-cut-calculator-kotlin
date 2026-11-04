package dev.auroralaboratories.fivecutmethod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.auroralaboratories.fivecutmethod.ui.theme.FiveCutTheme
import kotlin.math.abs
import kotlin.math.roundToInt

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

/**
 * The main screen of the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoodworkingHelperScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Woodworking Helper", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FiveCutCard()
            ConverterCard()
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
fun CardHeader(title: String, action: @Composable () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        action()
    }
}

@Composable
fun FieldGroup(label: String, content: @Composable RowScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Composable
fun MeasurementField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    suffix: String = "mm",
    hint: String? = null,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        placeholder = if (hint != null) {
            { Text(hint) }
        } else null,
        suffix = { Text(suffix) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontFeatureSettings = "tnum"),
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}



sealed interface CutResult {
    data object Incomplete : CutResult
    data object Square : CutResult
    data class Adjust(val amount: Float, val forwards: Boolean, val difference: Float) : CutResult
}

@Composable
fun FiveCutCard() {
    var nearSide by rememberSaveable { mutableStateOf("") }
    var farSide by rememberSaveable { mutableStateOf("") }
    var stripLength by rememberSaveable { mutableStateOf("") }
    var fenceLength by rememberSaveable { mutableStateOf("") }
    var showSteps by rememberSaveable { mutableStateOf(false) }

    val result = cutResult(
        nearSide.toNumberOrNull(),
        farSide.toNumberOrNull(),
        stripLength.toNumberOrNull(),
        fenceLength.toNumberOrNull()
    )
    val hasInput = listOf(nearSide, farSide, stripLength, fenceLength).any { it.isNotBlank() }

    SectionCard {
        CardHeader("Five-cut method") {
            if (hasInput) {
                TextButton(onClick = {
                    nearSide = ""
                    farSide = ""
                    stripLength = ""
                    fenceLength = ""
                }) { Text("Clear") }
            }
        }

        Column {
            Text(
                "Square your crosscut sled by measuring a test strip cut from a panel.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = { showSteps = !showSteps },
                modifier = Modifier.offset(x = (-12).dp)
            ) { Text(if (showSteps) "Hide steps" else "How it works") }
        }

        AnimatedVisibility(
            visible = showSteps,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                fiveCutSteps.forEachIndexed { index, step -> StepRow(index + 1, step) }
            }
        }

        FieldGroup("Strip width") {
            MeasurementField("Near end", nearSide, { nearSide = it }, Modifier.weight(1f))
            MeasurementField("Far end", farSide, { farSide = it }, Modifier.weight(1f))
        }
        FieldGroup("Length") {
            MeasurementField("Strip", stripLength, { stripLength = it }, Modifier.weight(1f))
            MeasurementField(
                "Fence",
                fenceLength,
                { fenceLength = it },
                Modifier.weight(1f),
                imeAction = ImeAction.Done
            )
        }

        ResultPanel(result)
    }
}

@Composable
fun StepRow(number: Int, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$number",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ResultPanel(result: CutResult) {
    val colors = MaterialTheme.colorScheme
    val container by animateColorAsState(
        targetValue = when (result) {
            CutResult.Incomplete -> colors.surfaceContainerHigh
            CutResult.Square -> colors.tertiaryContainer
            is CutResult.Adjust -> colors.primaryContainer
        },
        label = "resultContainer"
    )
    val content by animateColorAsState(
        targetValue = when (result) {
            CutResult.Incomplete -> colors.onSurfaceVariant
            CutResult.Square -> colors.onTertiaryContainer
            is CutResult.Adjust -> colors.onPrimaryContainer
        },
        label = "resultContent"
    )

    Surface(
        color = container,
        contentColor = content,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = result,
            contentKey = { it::class },
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.padding(20.dp),
            label = "result"
        ) { state ->
            when (state) {
                CutResult.Incomplete -> Text(
                    "Enter all four measurements to see the adjustment.",
                    style = MaterialTheme.typography.bodyMedium
                )

                CutResult.Square -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Square", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "No adjustment needed. Your sled is cutting true.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is CutResult.Adjust -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Move the fence", style = MaterialTheme.typography.labelLarge)
                    Row {
                        Text(
                            "%.2f".format(state.amount),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontFeatureSettings = "tnum"
                            ),
                            modifier = Modifier.alignByBaseline()
                        )
                        Text(
                            " mm",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                    Text(
                        if (state.forwards) "Forwards" else "Backwards",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Strip difference %.2f mm".format(state.difference),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}




// ---------- Converter ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterCard() {
    var input by rememberSaveable { mutableStateOf("") }
    var inchesToMm by rememberSaveable { mutableStateOf(true) }

    val primary: String?
    val secondary: String?
    if (inchesToMm) {
        val inches = parseInches(input)
        primary = inches?.let { "%.1f mm".format(it * 25.4f) }
        secondary = inches?.let { "%.3f in".format(it) }
    } else {
        val inches = input.toNumberOrNull()?.div(25.4f)
        primary = inches?.let { "%.3f in".format(it) }
        secondary = inches?.let { "≈ ${toFractionalInches(it)} to the nearest 1/32\"" }
    }

    SectionCard {
        CardHeader("Unit converter")

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = inchesToMm,
                onClick = {
                    inchesToMm = true
                    input = ""
                },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) { Text("in → mm") }
            SegmentedButton(
                selected = !inchesToMm,
                onClick = {
                    inchesToMm = false
                    input = ""
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) { Text("mm → in") }
        }

        MeasurementField(
            label = if (inchesToMm) "Inches" else "Millimetres",
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            suffix = if (inchesToMm) "in" else "mm",
            hint = if (inchesToMm) "e.g. 1 3/4" else null,
            keyboardType = if (inchesToMm) KeyboardType.Text else KeyboardType.Decimal,
            imeAction = ImeAction.Done
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (primary == null) {
                    Text(
                        if (inchesToMm) "Enter a length in inches. Fractions work too."
                        else "Enter a length in millimetres.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        primary,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFeatureSettings = "tnum"
                        )
                    )
                    if (secondary != null) {
                        Text(
                            secondary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


fun String.toNumberOrNull(): Float? = trim().replace(',', '.').toFloatOrNull()

/** Accepts "1.75", "1,75", "3/4", "1 3/4" and "1-3/4". */
fun parseInches(input: String): Float? {
    val text = input.trim().replace(',', '.').replace('-', ' ')
    if (text.isEmpty()) return null
    text.toFloatOrNull()?.let { return it }

    val parts = text.split(Regex("\\s+"))
    return when (parts.size) {
        1 -> parseFraction(parts[0])
        2 -> {
            val whole = parts[0].toFloatOrNull() ?: return null
            val fraction = parseFraction(parts[1]) ?: return null
            whole + fraction
        }
        else -> null
    }
}

fun parseFraction(text: String): Float? {
    val parts = text.split('/')
    if (parts.size != 2) return null
    val numerator = parts[0].toFloatOrNull() ?: return null
    val denominator = parts[1].toFloatOrNull()?.takeIf { it != 0f } ?: return null
    return numerator / denominator
}

fun toFractionalInches(inches: Float): String {
    val thirtySeconds = (inches * 32).roundToInt()
    val whole = thirtySeconds / 32
    var numerator = thirtySeconds % 32
    var denominator = 32
    while (numerator != 0 && numerator % 2 == 0) {
        numerator /= 2
        denominator /= 2
    }
    return when {
        numerator == 0 -> "$whole\""
        whole == 0 -> "$numerator/$denominator\""
        else -> "$whole $numerator/$denominator\""
    }
}
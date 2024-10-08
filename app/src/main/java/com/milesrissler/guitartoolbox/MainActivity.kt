package com.milesrissler.guitartoolbox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milesrissler.guitartoolbox.ui.theme.GuitarToolboxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent(padding: Dp = 24.dp, height: Dp = 64.dp)
{
    val keyText = remember { mutableStateOf("") }
    val capoText = remember { mutableStateOf("") }
    val resultTexts = remember { listOf(
        mutableStateOf("") to mutableStateOf(""), // 1
        mutableStateOf("") to mutableStateOf(""), // 2
        mutableStateOf("") to mutableStateOf(""), // 3
        mutableStateOf("") to mutableStateOf(""), // 4
        mutableStateOf("") to mutableStateOf(""), // 5
        mutableStateOf("") to mutableStateOf(""), // 6
        mutableStateOf("") to mutableStateOf("")  // 7
    ) }

    fun updateResults() = try {
        val key: String = keyText.value
        val capo: Int = capoText.value.toIntOrNull() ?: 0
        val keyWithCapo: String = transposeChord(
            chord = key,
            semitones = -capo
        )
        resultTexts.forEachIndexed { index, item ->
            item.first.value = transposeChordByWholeNotes(
                chord = key,
                wholeNotes = index
            )
            item.second.value = transposeChordByWholeNotes(
                chord = keyWithCapo,
                wholeNotes = index
            )
        }
    } catch (ex: Exception) {
        resultTexts.forEach { item ->
            item.first.value = ""
            item.second.value = ""
        }
        Log.e("Transpose", ex.message ?: "Unknown error")
    }

    GuitarToolboxTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(padding)
            ) {
                Text(
                    text = "Transpose",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(padding))
                Row {
                    RoundedTextField(
                        label = "Key",
                        text = keyText.value,
                        height = height,
                        modifier = Modifier
                            .weight(1f),
                        onTextChange = { newText ->
                            keyText.value = newText.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase() else it.toString()
                            }
                            updateResults()
                        }
                    )
                    Spacer(modifier = Modifier.width(padding))
                    RoundedTextField(
                        label = "Capo",
                        text = capoText.value,
                        height = height,
                        numeric = true,
                        modifier = Modifier
                            .weight(1f),
                        onTextChange = { newText ->
                            capoText.value = newText
                            updateResults()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(padding))
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Column (
                        Modifier.weight(1f)
                    ) {
                        TextChip(
                            text = resultTexts[0].first.value,
                            title = "1",
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[1].first.value,
                            title = "2",
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[2].first.value,
                            title = "3",
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[3].first.value,
                            title = "4",
                            emphasize = true,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[4].first.value,
                            title = "5",
                            emphasize = true,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[5].first.value,
                            title = "6",
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[6].first.value,
                            title = "7",
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(
                        Modifier
                            .weight(1f)
                    ) {
                        TextChip(
                            text = resultTexts[0].second.value,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[1].second.value,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[2].second.value,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[3].second.value,
                            emphasize = true,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[4].second.value,
                            emphasize = true,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[5].second.value,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                        Spacer(modifier = Modifier.height(padding))
                        TextChip(
                            text = resultTexts[6].second.value,
                            padding = padding,
                            modifier = Modifier.height(height)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoundedTextField(
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    text: String = "",
    height: Dp = 64.dp,
    numeric: Boolean = false
) {
    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            // Only update state if the new text is a valid integer or empty
            if (!numeric || newText.isEmpty() || newText.toIntOrNull() != null) {
                onTextChange(newText)
            }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(height),
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ),
        singleLine = true
    )
}

@Composable
fun TextChip(
    modifier: Modifier = Modifier,
    text: String = "",
    title: String = "",
    fontSize: TextUnit = 16.sp,
    padding: Dp = 16.dp,
    emphasize: Boolean = false
) {
    Row(
        modifier = modifier
            .background(
                color = if (emphasize) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                    alpha = 0.2f
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    1.dp,
                    if (emphasize) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        if (title.isNotBlank()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .background(
                        color = if (emphasize) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(start = padding, end = padding)
            ) {
                Text(
                    text = title,
                    fontSize = fontSize
                )
            }
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = padding / 2, end = padding / 2)
        ) {
            Text(
                text = text,
                color = if (emphasize) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                fontSize = fontSize
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    MainContent()
}

fun transposeChord(chord: String, semitones: Int): String {
    // Define the notes in an array
    val notes = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

    // Extract the root and the extension (if any)
    val root = chord.takeWhile { it.isLetter() || it == '#' }
    val extension = chord.dropWhile { it.isLetter() || it == '#' }

    // Find the index of the root note
    val rootIndex = notes.indexOf(root)

    if (rootIndex == -1) {
        throw IllegalArgumentException("Invalid chord: $chord")
    }

    // Calculate new root index with wrapping (handles negative values)
    val newRootIndex = (rootIndex + semitones + notes.size) % notes.size
    val newRoot = notes[newRootIndex]

    // Return the transposed chord
    return newRoot + extension
}

fun transposeChordByWholeNotes(chord: String, wholeNotes: Int): String {
    // Define the natural notes (no sharps)
    val notes = arrayOf("A", "B", "C", "D", "E", "F", "G")

    // Extract the root note and extension (if any)
    val root = chord.takeWhile { it.isLetter() }
    val extension = chord.dropWhile { it.isLetter() }

    // Find the index of the root note
    val rootIndex = notes.indexOf(root)

    if (rootIndex == -1) {
        throw IllegalArgumentException("Invalid chord: $chord")
    }

    // Calculate new root index with wrapping (handles negative values)
    val newRootIndex = (rootIndex + wholeNotes + notes.size) % notes.size
    val newRoot = notes[newRootIndex]

    // Return the transposed chord
    return newRoot + extension
}

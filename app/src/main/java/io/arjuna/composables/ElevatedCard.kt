package io.arjuna.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val CommonElevatedCardModifier = Modifier
    .padding(4.dp)
    .fillMaxWidth(0.9f)

@Composable
fun ElevatedCardTitle(title: String) =
    Text(title, Modifier.padding(5.dp), fontWeight = FontWeight.Bold)
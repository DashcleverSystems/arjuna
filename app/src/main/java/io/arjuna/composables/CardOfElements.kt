package io.arjuna.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> OptionalContent(
    elements: Set<T>,
    onEmptyContent: @Composable (() -> Unit)?,
    elementComposable: @Composable (T) -> Unit,
) {
    onEmptyContent?.let { if (elements.isEmpty()) it.invoke() }
    elements.forEach { elementComposable(it) }
}

@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    content: @Composable ColumnScope.() -> Unit
) {
    androidx.compose.material3.OutlinedCard(
        modifier
            .padding(1.dp)
            .fillMaxWidth(0.7f),
        colors = colors
    ) {
        content()
    }
}
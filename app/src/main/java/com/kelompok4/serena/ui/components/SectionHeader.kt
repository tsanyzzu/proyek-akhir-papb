package com.example.serena.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/**
 * A simple composable that displays a section title with an optional
 * clickable "See all" action on the right.  This is used for the
 * Artikel and Kegiatan sections on the SelfCare screen.
 *
 * @param title The title of the section.
 * @param onClickSeeAll Optional lambda invoked when the "See all" text is clicked.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onClickSeeAll: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        onClickSeeAll?.let { callback ->
            Text(
                text = "Lihat semua",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { callback() }
            )
        }
    }
}
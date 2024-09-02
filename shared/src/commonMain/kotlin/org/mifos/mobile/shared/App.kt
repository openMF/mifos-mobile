package org.mifos.mobile.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Mifos Mobile",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}


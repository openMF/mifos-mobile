
package cmp.navigation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color.White,
    ) {
        Box(modifier = modifier.fillMaxSize())
    }
}

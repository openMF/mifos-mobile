package org.mifos.mobile.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FaqItemHolder(
    question: String?,
    answer: String?,
    isSelected: Boolean,
    onItemSelected: (Int) -> Unit,
    index: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onItemSelected.invoke(index)
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "drop down",
                tint = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                modifier = Modifier
                    .scale(1f, if (isSelected) -1f else 1f)
            )
        }

        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Text(
                text = answer.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        Divider()
    }
}

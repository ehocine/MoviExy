package com.helic.moviexy.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.helic.moviexy.presentation.ui.theme.DROP_DOWN_HEIGHT
import com.helic.moviexy.presentation.ui.theme.MediumGray
import com.helic.moviexy.presentation.ui.theme.firstColor


@Composable
fun DropDownOptions(
    label: String,
    optionsList: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(label) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .onGloballyPositioned {
                parentSize = it.size
            }
            .background(firstColor)
            .height(DROP_DOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = ContentAlpha.disabled
                ),
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(weight = 8f)
                .padding(start = 10.dp),
        ) {
            Text(
                text = selectedOption,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
                color = MediumGray
            )
        }
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(degrees = angle)
                .weight(weight = 1.5f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop Down Arrow",
                tint = MediumGray
            )
        }
        DropdownMenu(
            modifier = Modifier
                .width(with(LocalDensity.current) { parentSize.width.toDp() })
                .background(firstColor),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            optionsList.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedOption = option
                        onOptionSelected(option)
                    },
                    Modifier.background(firstColor)
                ) {
                    Text(text = option, color = MediumGray)
                }
            }
        }
    }
}
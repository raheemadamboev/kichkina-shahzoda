package xyz.teamgravity.kichkinashahzoda.presentation.component.slider

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SongSlider(
    position: Long,
    positionText: String,
    positionUser: Long,
    positionUserText: String,
    duration: Long,
    durationText: String,
    onPositionUserChange: (value: Long) -> Unit,
    onPositionUserChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val dragged by interaction.collectIsDraggedAsState()
    val interacting = pressed || dragged

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = if (interacting) positionUserText else positionText)
        Spacer(modifier = Modifier.width(16.dp))
        Slider(
            value = if (interacting) positionUser.toFloat() else position.toFloat(),
            valueRange = 0F..duration.toFloat(),
            onValueChange = { onPositionUserChange(it.toLong()) },
            onValueChangeFinished = onPositionUserChangeFinished,
            interactionSource = interaction,
            modifier = Modifier.weight(1F)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = durationText)
    }
}
package xyz.teamgravity.kichkinashahzoda.presentation.component.misc

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.teamgravity.kichkinashahzoda.R

@Composable
fun SongController(
    playing: Boolean,
    onPreviousSong: () -> Unit,
    onPlayPause: () -> Unit,
    onNextSong: () -> Unit,
    modifier: Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        FilledTonalIconButton(
            onClick = onPreviousSong,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = stringResource(id = R.string.cd_skip_previous)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        FilledTonalIconButton(
            onClick = onPlayPause,
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(id = R.string.cd_play_pause),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        FilledTonalIconButton(
            onClick = onNextSong,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = stringResource(id = R.string.cd_skip_next)
            )
        }
    }
}
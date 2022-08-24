package xyz.teamgravity.kichkinashahzoda.presentation.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel
import xyz.teamgravity.kichkinashahzoda.presentation.component.text.TextPlain

@Composable
fun CardSong(
    song: SongModel,
    onClick: (song: SongModel) -> Unit,
) {
    Card(
        onClick = { onClick(song) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.sticker_album_cover),
                contentDescription = stringResource(id = R.string.cd_album_cover),
                modifier = Modifier.size(75.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            TextPlain(id = song.name)
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}
package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.constant.SongConst
import xyz.teamgravity.kichkinashahzoda.presentation.component.misc.SongController
import xyz.teamgravity.kichkinashahzoda.presentation.component.slider.SongSlider
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBar

@Composable
fun SongPortraitScreen(
    onBackButtonClick: () -> Unit,
    viewmodel: SongViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(
                        id = R.string.app_name
                    )
                },
                navigationIcon = {
                    IconButtonPlain(
                        onClick = onBackButtonClick,
                        icon = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = R.string.cd_back_button
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = viewmodel.song?.name ?: SongConst.NAME_1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(id = R.string.cd_album_cover),
                modifier = Modifier.size(300.dp)
            )
            SongSlider(
                position = viewmodel.position,
                positionText = viewmodel.positionText,
                positionUser = viewmodel.positionUser,
                positionUserText = viewmodel.positionUserText,
                duration = viewmodel.duration,
                durationText = viewmodel.durationText,
                onPositionUserChange = viewmodel::onPositionUserChange,
                onPositionUserChangeFinished = viewmodel::onSeek,
                modifier = Modifier.fillMaxWidth()
            )
            SongController(
                playing = viewmodel.playing,
                onPreviousSong = viewmodel::onPreviousSong,
                onPlayPause = viewmodel::onPlayPause,
                onNextSong = viewmodel::onNextSong,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
package xyz.teamgravity.kichkinashahzoda.presentation.screen.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.constant.SongConst
import xyz.teamgravity.kichkinashahzoda.presentation.component.button.IconButtonPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.misc.SongController
import xyz.teamgravity.kichkinashahzoda.presentation.component.slider.SongSlider
import xyz.teamgravity.kichkinashahzoda.presentation.component.text.TextPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBar
import xyz.teamgravity.kichkinashahzoda.presentation.viewmodel.SongViewModel

@Composable
fun SongLandscapeScreen(
    onBackButtonClick: () -> Unit,
    viewmodel: SongViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(id = R.string.app_name)
                },
                navigationIcon = {
                    IconButtonPlain(
                        onClick = onBackButtonClick,
                        icon = Icons.Default.ArrowBack,
                        contentDescription = R.string.cd_back_button
                    )
                }
            )
        }
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (coverI, nameT, sliderC, controllerC) = createRefs()
            val oneG = createGuidelineFromStart(0.4F)

            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(id = R.string.cd_album_cover),
                modifier = Modifier.constrainAs(coverI) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    linkTo(start = parent.start, end = oneG, startMargin = 20.dp, endMargin = 20.dp)
                    linkTo(top = parent.top, bottom = parent.bottom, topMargin = 20.dp, bottomMargin = 20.dp)
                }
            )
            Text(
                text = viewmodel.song?.name ?: SongConst.NAME_1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(nameT) {
                    width = Dimension.fillToConstraints
                    linkTo(start = oneG, end = parent.end, startMargin = 20.dp, endMargin = 20.dp)
                }
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
                modifier = Modifier.constrainAs(sliderC) {
                    width = Dimension.fillToConstraints
                    linkTo(start = oneG, end = parent.end, startMargin = 20.dp, endMargin = 20.dp)
                }
            )
            SongController(
                playing = viewmodel.playing,
                onPreviousSong = viewmodel::onPreviousSong,
                onPlayPause = viewmodel::onPlayPause,
                onNextSong = viewmodel::onNextSong,
                modifier = Modifier.constrainAs(controllerC) {
                    width = Dimension.fillToConstraints
                    linkTo(start = oneG, end = parent.end, startMargin = 20.dp, endMargin = 20.dp)
                }
            )

            createVerticalChain(nameT, sliderC, controllerC)
        }
    }
}
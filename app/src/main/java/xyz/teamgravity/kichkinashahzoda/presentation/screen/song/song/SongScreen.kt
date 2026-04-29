package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.song

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.coresdkcompose.configuration.screen.ScreenConfiguration
import xyz.teamgravity.coresdkcompose.configuration.screen.getScreenConfiguration
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>
@Composable
fun SongScreen(
    navigator: DestinationsNavigator
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val onBackButtonClick: () -> Unit = remember { { dispatcher?.onBackPressed() ?: navigator.navigateUp() } }

    when (getScreenConfiguration()) {
        ScreenConfiguration.PhonePortrait, ScreenConfiguration.TabletPortrait -> {
            SongPortraitScreen(
                onBackButtonClick = onBackButtonClick
            )
        }

        else -> {
            SongLandscapeScreen(
                onBackButtonClick = onBackButtonClick
            )
        }
    }
}
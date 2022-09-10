package xyz.teamgravity.kichkinashahzoda.presentation.screen.song

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph

@MainNavGraph
@Destination
@Composable
fun SongScreen(
    navigator: DestinationsNavigator,
) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> SongPortraitScreen(onBackButtonClick = navigator::popBackStack)
        else -> SongLandscapeScreen(onBackButtonClick = navigator::popBackStack)
    }
}
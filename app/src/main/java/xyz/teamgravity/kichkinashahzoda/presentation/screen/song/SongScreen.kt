package xyz.teamgravity.kichkinashahzoda.presentation.screen.song

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.kichkinashahzoda.presentation.component.misc.WindowInfo
import xyz.teamgravity.kichkinashahzoda.presentation.component.misc.rememberWindowInfo
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph
import xyz.teamgravity.kichkinashahzoda.presentation.screen.support.SupportLandscapeScreen
import xyz.teamgravity.kichkinashahzoda.presentation.screen.support.SupportPortraitScreen

@MainNavGraph
@Destination
@Composable
fun SongScreen(
    navigator: DestinationsNavigator,
) {
    when (rememberWindowInfo().screenWidthInfo) {
        WindowInfo.WindowType.Compact -> SupportPortraitScreen(onBackButtonClick = navigator::popBackStack)
        else -> SupportLandscapeScreen(onBackButtonClick = navigator::popBackStack)
    }
}
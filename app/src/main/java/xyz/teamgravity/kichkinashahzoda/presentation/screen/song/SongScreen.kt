package xyz.teamgravity.kichkinashahzoda.presentation.screen.song

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.kichkinashahzoda.presentation.component.misc.WindowInfo
import xyz.teamgravity.kichkinashahzoda.presentation.component.misc.rememberWindowInfo
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph

@MainNavGraph
@Destination(navArgsDelegate = SongScreenNavArgs::class)
@Composable
fun SongScreen(
    navigator: DestinationsNavigator,
) {
    when (rememberWindowInfo().screenWidthInfo) {
        WindowInfo.WindowType.Compact -> SongPortraitScreen(onBackButtonClick = navigator::popBackStack)
        else -> SongLandscapeScreen(onBackButtonClick = navigator::popBackStack)
    }
}

data class SongScreenNavArgs(
    val id: Int,
)
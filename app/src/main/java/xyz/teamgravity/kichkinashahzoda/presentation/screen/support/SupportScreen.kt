package xyz.teamgravity.kichkinashahzoda.presentation.screen.support

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
fun SupportScreen(
    navigator: DestinationsNavigator,
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val onBackButtonClick: () -> Unit = remember { { dispatcher?.onBackPressed() ?: navigator.navigateUp() } }

    when (getScreenConfiguration()) {
        ScreenConfiguration.PhonePortrait, ScreenConfiguration.TabletPortrait -> {
            SupportPortraitScreen(
                onBackButtonClick = onBackButtonClick
            )
        }

        else -> {
            SupportLandscapeScreen(
                onBackButtonClick = onBackButtonClick
            )
        }
    }
}
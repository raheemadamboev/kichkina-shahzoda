package xyz.teamgravity.kichkinashahzoda.presentation.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.util.Helper
import xyz.teamgravity.kichkinashahzoda.presentation.component.text.TextPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBar
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBarMoreMenu
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph
import xyz.teamgravity.kichkinashahzoda.presentation.screen.destinations.AboutScreenDestination
import xyz.teamgravity.kichkinashahzoda.presentation.screen.destinations.SupportScreenDestination
import xyz.teamgravity.kichkinashahzoda.presentation.viewmodel.SongListViewModel

@MainNavGraph(start = true)
@Destination
@Composable
fun SongListScreen(
    navigator: DestinationsNavigator,
    viewmodel: SongListViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(id = R.string.app_name)
                },
                actions = {
                    TopBarMoreMenu(
                        expanded = viewmodel.menuExpanded,
                        onExpand = viewmodel::onMenuExpanded,
                        onDismiss = viewmodel::onMenuCollapsed,
                        onSupportClick = {
                            navigator.navigate(SupportScreenDestination)
                            viewmodel.onMenuCollapsed()
                        },
                        onShareClick = {
                            Helper.shareApp(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onRateClick = {
                            Helper.rateApp(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onSourceCodeClick = {
                            Helper.viewSourceCode(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onAboutClick = {
                            navigator.navigate(AboutScreenDestination)
                            viewmodel.onMenuCollapsed()
                        }
                    )
                }
            )
        }
    ) {

    }
}
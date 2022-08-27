package xyz.teamgravity.kichkinashahzoda.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.constant.SongConst
import xyz.teamgravity.kichkinashahzoda.core.util.Helper
import xyz.teamgravity.kichkinashahzoda.presentation.component.button.IconButtonPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.card.CardSong
import xyz.teamgravity.kichkinashahzoda.presentation.component.text.TextPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBar
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBarMoreMenu
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph
import xyz.teamgravity.kichkinashahzoda.presentation.screen.destinations.AboutScreenDestination
import xyz.teamgravity.kichkinashahzoda.presentation.screen.destinations.SongScreenDestination
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
                        onExpand = viewmodel::onMenuExpand,
                        onDismiss = viewmodel::onMenuCollapse,
                        onSupportClick = {
                            navigator.navigate(SupportScreenDestination)
                            viewmodel.onMenuCollapse()
                        },
                        onShareClick = {
                            Helper.shareApp(context)
                            viewmodel.onMenuCollapse()
                        },
                        onRateClick = {
                            Helper.rateApp(context)
                            viewmodel.onMenuCollapse()
                        },
                        onSourceCodeClick = {
                            Helper.viewSourceCode(context)
                            viewmodel.onMenuCollapse()
                        },
                        onAboutClick = {
                            navigator.navigate(AboutScreenDestination)
                            viewmodel.onMenuCollapse()
                        }
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1F)
            ) {
                items(viewmodel.songs) { song ->
                    CardSong(
                        song = song,
                        onClick = viewmodel::onSongClick
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { navigator.navigate(SongScreenDestination) }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (viewmodel.currentSong == null) SongConst.NAME_1 else viewmodel.currentSong!!.name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1F)
                )
                Spacer(modifier = Modifier.width(20.dp))
                IconButtonPlain(
                    onClick = viewmodel::onPlayPause,
                    icon = if (viewmodel.playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = R.string.cd_play_pause,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
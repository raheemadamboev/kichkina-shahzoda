package xyz.teamgravity.kichkinashahzoda.presentation.screen.song.list

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SongScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SupportScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.coresdkandroid.connect.ConnectUtil
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.coresdkcompose.review.DialogReview
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.coresdkcompose.update.DialogUpdateAvailable
import xyz.teamgravity.coresdkcompose.update.DialogUpdateDownloaded
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.constant.SongConst
import xyz.teamgravity.kichkinashahzoda.core.util.Helper
import xyz.teamgravity.kichkinashahzoda.presentation.component.card.CardSong
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBar
import xyz.teamgravity.kichkinashahzoda.presentation.component.topbar.TopBarMoreMenu
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>(start = true)
@Composable
fun SongListScreen(
    navigator: DestinationsNavigator,
    viewmodel: SongListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val direction = LocalLayoutDirection.current
    val updateLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {}
    )

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                SongListViewModel.SongListEvent.Review -> {
                    viewmodel.onReview(activity)
                }

                SongListViewModel.SongListEvent.DownloadAppUpdate -> {
                    viewmodel.onUpdateDownload(updateLauncher)
                }
            }
        }
    )

    LifecycleEventEffect(
        event = Lifecycle.Event.ON_RESUME,
        onEvent = viewmodel::onUpdateCheck
    )

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(
                        id = R.string.app_name
                    )
                },
                actions = {
                    TopBarMoreMenu(
                        expanded = viewmodel.menuExpanded,
                        onExpand = viewmodel::onMenuExpand,
                        onDismiss = viewmodel::onMenuCollapse,
                        onSupport = {
                            navigator.navigate(SupportScreenDestination)
                        },
                        onShare = {
                            Helper.shareApp(context)
                        },
                        onRate = {
                            ConnectUtil.viewAppPlayStorePage(context)
                        },
                        onSourceCode = {
                            Helper.viewSourceCode(context)
                        },
                        onAbout = {
                            navigator.navigate(AboutScreenDestination)
                        }
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(direction),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(direction)
                )
        ) {
            LazyColumn(
                modifier = Modifier.weight(1F)
            ) {
                items(
                    items = viewmodel.songs,
                    key = { it.id }
                ) { song ->
                    CardSong(
                        song = song,
                        onClick = viewmodel::onSongHandle
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        navigator.navigate(SongScreenDestination)
                    }
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
            ) {
                Text(
                    text = viewmodel.currentSong?.name ?: SongConst.NAME_1,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1F)
                )
                Spacer(
                    modifier = Modifier.width(20.dp)
                )
                IconButtonPlain(
                    onClick = viewmodel::onPlayPause,
                    icon = if (viewmodel.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = R.string.cd_play_pause,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(padding.calculateBottomPadding())
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        DialogReview(
            visible = viewmodel.reviewShown,
            onDismiss = viewmodel::onReviewDismiss,
            onDeny = viewmodel::onReviewDeny,
            onRemindLater = viewmodel::onReviewLater,
            onReview = viewmodel::onReviewConfirm
        )
        DialogUpdateAvailable(
            type = viewmodel.updateAvailableType,
            onDismiss = viewmodel::onUpdateAvailableDismiss,
            onConfirm = viewmodel::onUpdateAvailableConfirm
        )
        DialogUpdateDownloaded(
            visible = viewmodel.updateDownloadedShown,
            onDismiss = viewmodel::onUpdateDownloadedDismiss,
            onConfirm = viewmodel::onUpdateInstall
        )
    }
}
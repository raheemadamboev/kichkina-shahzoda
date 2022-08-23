package xyz.teamgravity.kichkinashahzoda.presentation.navigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost
import xyz.teamgravity.kichkinashahzoda.presentation.screen.NavGraphs

@Composable
fun Navigation() {
    DestinationsNavHost(navGraph = NavGraphs.main)
}
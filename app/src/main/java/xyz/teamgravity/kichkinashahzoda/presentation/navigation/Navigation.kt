package xyz.teamgravity.kichkinashahzoda.presentation.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.SongListScreenDestination
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.utils.findDestination
import xyz.teamgravity.coresdkandroid.android.setDarkNavigationBar
import xyz.teamgravity.coresdkandroid.android.setLightNavigationBar

@Composable
fun Navigation(
    engine: NavHostEngine = rememberNavHostEngine(),
    controller: NavHostController = engine.rememberNavController()
) {
    val activity = LocalActivity.current
    val darkMode = isSystemInDarkTheme()

    DisposableEffect(
        key1 = controller,
        key2 = activity,
        key3 = darkMode,
        effect = {
            val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                destination.route?.let { route ->
                    when (NavGraphs.main.findDestination(route)) {
                        SongListScreenDestination -> if (darkMode) activity?.setLightNavigationBar() else activity?.setDarkNavigationBar()
                        else -> if (darkMode) activity?.setDarkNavigationBar() else activity?.setLightNavigationBar()
                    }
                }
            }
            controller.addOnDestinationChangedListener(listener)

            onDispose {
                controller.removeOnDestinationChangedListener(listener)
            }
        }
    )

    DestinationsNavHost(
        navGraph = NavGraphs.main,
        engine = engine,
        navController = controller
    )
}
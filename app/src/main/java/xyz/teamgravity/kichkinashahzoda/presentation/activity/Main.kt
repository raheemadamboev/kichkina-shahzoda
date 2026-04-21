package xyz.teamgravity.kichkinashahzoda.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.coresdkandroid.android.isDarkMode
import xyz.teamgravity.coresdkandroid.android.setDarkStatusBar
import xyz.teamgravity.coresdkandroid.android.setLightStatusBar
import xyz.teamgravity.coresdkandroid.android.setNavigationBarTransparent
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.Navigation
import xyz.teamgravity.kichkinashahzoda.presentation.theme.KichkinaShahzodaTheme

@AndroidEntryPoint
class Main : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (isDarkMode()) setLightStatusBar() else setDarkStatusBar()
        setNavigationBarTransparent()

        setContent {
            KichkinaShahzodaTheme {
                Navigation()
            }
        }
    }
}
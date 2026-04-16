package xyz.teamgravity.kichkinashahzoda.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.kichkinashahzoda.presentation.navigation.Navigation
import xyz.teamgravity.kichkinashahzoda.presentation.theme.KichkinaShahzodaTheme

@AndroidEntryPoint
class Main : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KichkinaShahzodaTheme {
                Navigation()
            }
        }
    }
}
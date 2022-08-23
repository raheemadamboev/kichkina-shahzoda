package xyz.teamgravity.kichkinashahzoda.presentation.navigation

import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph(default = true)
annotation class MainNavGraph(
    val start: Boolean = false,
)
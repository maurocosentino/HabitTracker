package com.mauro.habittracker.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mauro.habittracker.feature.habits.HabitsScreen
import com.mauro.habittracker.feature.statistics.StatisticsScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Habits : Screen("habits", "Habits", Icons.Filled.Home)
    data object Statistics : Screen("statistics", "Stats", Icons.Filled.DateRange)
}

private val bottomNavItems = listOf(Screen.Habits, Screen.Statistics)

private const val ANIM_DURATION = 300

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HabitNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Habits.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = Screen.Habits.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeIn(tween(ANIM_DURATION))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeOut(tween(ANIM_DURATION))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeIn(tween(ANIM_DURATION))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeOut(tween(ANIM_DURATION))
                }
            ) {
                HabitsScreen()
            }

            composable(
                route = Screen.Statistics.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeIn(tween(ANIM_DURATION))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeOut(tween(ANIM_DURATION))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeIn(tween(ANIM_DURATION))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(ANIM_DURATION)
                    ) + fadeOut(tween(ANIM_DURATION))
                }
            ) {
                StatisticsScreen()
            }
        }
    }
}
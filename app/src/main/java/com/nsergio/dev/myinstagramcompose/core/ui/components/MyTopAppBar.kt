package com.nsergio.dev.myinstagramcompose.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nsergio.dev.myinstagramcompose.navigation.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavHostController) {

    val noTopBarRoutes = setOf(
        AppDestination.Login.route,
        AppDestination.PhotoViewer.route
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val canNavigateBack = navController.previousBackStackEntry != null
    val showTopBar = currentRoute !in noTopBarRoutes

    if (showTopBar) {
        TopAppBar(
            title = { Text(currentRoute) },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        )
    }
}
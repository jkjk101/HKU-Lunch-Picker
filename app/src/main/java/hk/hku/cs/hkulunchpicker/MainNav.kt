package hk.hku.cs.hkulunchpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(
    landing: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val screen1 = "Home"
    val screen2 = "Random Draw"
    val screen3 = "Featured Restaurants"

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: landing


    println("--------------------")
    navController.currentBackStack.value.forEach {
        println("screen : ${it.destination.route}")
    }
    println("--------------------")

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 50.dp, end = 0.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = screen1,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    selected = currentRoute == screen1,
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(screen1)
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(start = 8.dp, top = 10.dp, end = 8.dp, bottom = 0.dp)
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = screen2,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    selected = currentRoute == screen2,
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(screen2)
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(start = 8.dp, top = 5.dp, end = 8.dp, bottom = 0.dp)
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = screen3,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    selected = currentRoute == screen3,
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(screen3)
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(start = 8.dp, top = 5.dp, end = 8.dp, bottom = 0.dp)
                )

            }
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = currentRoute, style = MaterialTheme.typography.bodyLarge) },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = { IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }, content = {
                        Icon(
                            imageVector = Icons.Default.Menu, contentDescription = null
                        )
                    })
                    }, colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )
            }, modifier = Modifier
        ) {
            NavHost(
                navController = navController, startDestination = landing, modifier = Modifier.padding(it)
            ) {

                composable(screen1) {
                    HomeScreen()
                }

                composable(screen2) {
                    RandomScreen()
                }

                composable(screen3) {
                    FeaturedScreen()
                }

            }
        }
    }
}




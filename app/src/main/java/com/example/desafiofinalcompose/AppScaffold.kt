package com.example.desafiofinalcompose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.borrame.ui.home.PantallaMaps
import com.example.desafiofinalcompose.ViewModels.BarmanViewModel
import com.example.loginfirebase_25_26.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavHostController) {

    val usuario = DatosCompartidos.usuario

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModelLogin: LoginViewModel = viewModel()

    val barmanViewModel: BarmanViewModel = viewModel()
    val comandasPendientes by barmanViewModel.comandasPendientes.collectAsState()

    val mostrarEstructura = currentRoute != Rutas.pantallaLogin
            && currentRoute != Rutas.pantallaRegistro

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = mostrarEstructura,
        drawerContent = {
            ModalDrawerSheet {
                if (mostrarEstructura) {

                    Text(
                        text = "Hola, ${usuario?.nombre ?: ""}",
                        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 16.dp)
                    )

                    when (usuario?.rol) {

                        1 -> {
                            NavigationDrawerItem(
                                label = { Text("Gestionar Usuarios") },
                                selected = currentRoute == Rutas.pantallaAdminUsuarios,
                                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaAdminUsuarios) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Gestionar Productos") },
                                selected = currentRoute == Rutas.pantallaAdminProductos,
                                icon = { Icon(Icons.Default.List, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaAdminProductos) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Gestionar Comandas") },
                                selected = currentRoute == Rutas.pantallaAdminComandas,
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaAdminComandas) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }

                        2 -> {
                            NavigationDrawerItem(
                                label = { Text("Crear Comanda") },
                                selected = currentRoute == Rutas.pantallaCamarero,
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaCamarero) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Historial Comandas") },
                                selected = currentRoute == Rutas.pantallaHistorial,
                                icon = { Icon(Icons.Default.List, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaHistorial) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }

                        3 -> {
                            NavigationDrawerItem(
                                label = { Text("Comandas Pendientes") },
                                selected = currentRoute == Rutas.pantallaBarman,
                                icon = {
                                    BadgedBox(badge = {
                                        if (comandasPendientes.isNotEmpty()) {
                                            Badge { Text(comandasPendientes.size.toString()) }
                                        }
                                    }) {
                                        Icon(Icons.Default.Home, contentDescription = null)
                                    }
                                },
                                onClick = {
                                    navController.navigate(Rutas.pantallaBarman) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }

                        4 -> {
                            NavigationDrawerItem(
                                label = { Text("Carta") },
                                selected = currentRoute == Rutas.pantallaCliente,
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaCliente) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Mis Pedidos") },
                                selected = currentRoute == Rutas.pantallaHistorial,
                                icon = { Icon(Icons.Default.List, contentDescription = null) },
                                onClick = {
                                    navController.navigate(Rutas.pantallaHistorial) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }

                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión") },
                        selected = false,
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            viewModelLogin.signOut(context)
                            navController.navigate(Rutas.pantallaLogin) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (mostrarEstructura) {
                    AppTopBar(
                        titulo = tituloSegunRuta(currentRoute),
                        onAbrirDrawer = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        },
                        onMenuPuntos = { expanded = true },
                        expanded = expanded,
                        onDismissMenu = { expanded = false },
                        onCerrarSesion = {
                            expanded = false
                            viewModelLogin.signOut(context)
                            navController.navigate(Rutas.pantallaLogin) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (mostrarEstructura) {
                    AppBottomBar(
                        rol = usuario?.rol ?: 0,
                        currentRoute = currentRoute,
                        navController = navController,
                        numeroComandasPendientes = comandasPendientes.size
                    )
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Rutas.pantallaLogin,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.pantallaLogin) {
                    PantallaLogin(navController)
                }
                composable(Rutas.pantallaRegistro) {
                    PantallaRegistro(navController)
                }
                composable(Rutas.pantallaHome) {
                    PantallaHome(navController)
                }
                composable(Rutas.pantallaAdmin) {
                    PantallaAdmin(navController)
                }
                composable(Rutas.pantallaCamarero) {
                    PantallaCamarero(navController)
                }
                composable(Rutas.pantallaBarman) {
                    PantallaBarman(navController)
                }
                composable(Rutas.pantallaCliente) {
                    PantallaCliente(navController)
                }
                composable(Rutas.pantallaHistorial) {
                    historicoComandas(navController, DatosCompartidos.usuario?.rol ?: 0)
                }
                composable(Rutas.pantallaAdminUsuarios) {
                    PantallaAdminUsuarios(navController)
                }
                composable(Rutas.pantallaAdminProductos) {
                    PantallaAdminProductos(navController)
                }
                composable(Rutas.pantallaAdminComandas) {
                    PantallaAdminComandas(navController)
                }
                composable(Rutas.pantallaCrearProducto) {
                    PantallaCrearProducto(navController)
                }
                composable(Rutas.pantallaMapa) {
                    PantallaMaps(navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onAbrirDrawer: () -> Unit,
    onMenuPuntos: () -> Unit,
    expanded: Boolean,
    onDismissMenu: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    TopAppBar(
        title = { Text(titulo) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF84F19),
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        ),
        navigationIcon = {
            IconButton(onClick = onAbrirDrawer) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onMenuPuntos) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissMenu
            ) {
                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = onCerrarSesion
                )
            }
        }
    )
}

@Composable
fun AppBottomBar(
    rol: Int,
    currentRoute: String?,
    navController: NavHostController,
    numeroComandasPendientes: Int = 0
) {
    when (rol) {

        1 -> {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaAdminUsuarios,
                    onClick = {
                        navController.navigate(Rutas.pantallaAdminUsuarios) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Usuarios") }
                )
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaAdminProductos,
                    onClick = {
                        navController.navigate(Rutas.pantallaAdminProductos) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Productos") }
                )
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaAdminComandas,
                    onClick = {
                        navController.navigate(Rutas.pantallaAdminComandas) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Comandas") }
                )
            }
        }

        2 -> {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaCamarero,
                    onClick = {
                        navController.navigate(Rutas.pantallaCamarero) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Comanda") }
                )
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaHistorial,
                    onClick = {
                        navController.navigate(Rutas.pantallaHistorial) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Historial") }
                )
            }
        }

        3 -> {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaBarman,
                    onClick = {
                        navController.navigate(Rutas.pantallaBarman) {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        BadgedBox(badge = {
                            if (numeroComandasPendientes > 0) {
                                Badge { Text(numeroComandasPendientes.toString()) }
                            }
                        }) {
                            Icon(Icons.Default.Home, contentDescription = null)
                        }
                    },
                    label = { Text("Pendientes") }
                )
            }
        }

        4 -> {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaCliente,
                    onClick = {
                        navController.navigate(Rutas.pantallaCliente) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Carta") }
                )
                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaHistorial,
                    onClick = {
                        navController.navigate(Rutas.pantallaHistorial) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Mis pedidos") }
                )
            }
        }
    }
}

private fun tituloSegunRuta(ruta: String?): String {
    return when (ruta) {
        Rutas.pantallaAdminComandas    -> "Comandas"
        Rutas.pantallaAdminUsuarios  -> "Usuarios"
        Rutas.pantallaAdminProductos -> "Productos"
        Rutas.pantallaCamarero       -> "Camarero"
        Rutas.pantallaBarman         -> "Barman"
        Rutas.pantallaCliente        -> "Carta"
        Rutas.pantallaHistorial      -> "Historial"
        Rutas.pantallaCrearProducto  -> "Nuevo producto"
        Rutas.pantallaMapa           -> "Seleccionar ubicación"
        else                         -> "Restaurante"
    }
}
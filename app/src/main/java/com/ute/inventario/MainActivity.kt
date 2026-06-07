package com.ute.inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.ute.inventario.presentation.components.LoadingScreen
import com.ute.inventario.presentation.navigation.Screen
import com.ute.inventario.presentation.ui.auth.LoginScreen
import com.ute.inventario.presentation.ui.auth.RegisterScreen
import com.ute.inventario.presentation.ui.producto.ProductoScreen
import com.ute.inventario.presentation.ui.admin.*
import com.ute.inventario.presentation.ui.categoria.CategoriaScreen
import com.ute.inventario.presentation.ui.proveedor.ProveedorScreen
import com.ute.inventario.presentation.ui.movimiento.MovimientoScreen
import com.ute.inventario.presentation.ui.ordencompra.OrdenCompraScreen
import com.ute.inventario.presentation.ui.user.UserScreen
import com.ute.inventario.presentation.viewmodel.*
import com.ute.inventario.theme.InventarioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventarioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    
    val productoViewModel: ProductoViewModel = hiltViewModel()
    val categoriaViewModel: CategoriaViewModel = hiltViewModel()
    val proveedorViewModel: ProveedorViewModel = hiltViewModel()
    val movimientoViewModel: MovimientoInventarioViewModel = hiltViewModel()
    val ordenViewModel: OrdenCompraViewModel = hiltViewModel()
    
    val isCheckingSession by authViewModel.isCheckingSession.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    val navController = rememberNavController()

    if (isCheckingSession) {
        LoadingScreen(message = "Iniciando...")
        return
    }

    if (!isAuthenticated) {
        NavHost(navController = navController, startDestination = Screen.Login.route) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { /* Autenticado */ },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = { /* Autenticado */ },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
        }
    } else {
        val navItems = remember(currentUser) {
            val items = mutableListOf(
                NavigationItem("Stock", Screen.ProductoScreen.route, Icons.Default.Inventory),
                NavigationItem("Categorías", Screen.CategoriaScreen.route, Icons.Default.Category),
                NavigationItem("Movimientos", Screen.MovimientoScreen.route, Icons.Default.History),
            )
            if (currentUser?.isStaff == true) {
                items.add(NavigationItem("Órdenes", Screen.OrdenCompraScreen.route, Icons.Default.ReceiptLong))
                items.add(NavigationItem("Proveedores", Screen.ProveedorScreen.route, Icons.Default.Business))
            }
            items.add(NavigationItem("Perfil", Screen.UserScreen.route, Icons.Default.Person))
            items
        }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    navItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
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
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.ProductoScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Pantallas Principales
                composable(Screen.ProductoScreen.route) { 
                    ProductoScreen(
                        viewModel = productoViewModel,
                        authViewModel = authViewModel, 
                        onNavigateToCreate = { navController.navigate(Screen.CreateProduct.route) },
                        onNavigateToEdit = { id -> navController.navigate(Screen.EditProduct().createRoute(id)) }
                    ) 
                }
                composable(Screen.CategoriaScreen.route) { 
                    CategoriaScreen(
                        viewModel = categoriaViewModel,
                        authViewModel = authViewModel,
                        onNavigateToCreate = { navController.navigate(Screen.CreateCategory.route) },
                        onNavigateToEdit = { id -> navController.navigate(Screen.EditCategory().createRoute(id)) }
                    ) 
                }
                composable(Screen.ProveedorScreen.route) { 
                    ProveedorScreen(
                        viewModel = proveedorViewModel,
                        authViewModel = authViewModel,
                        onNavigateToCreate = { navController.navigate(Screen.CreateProveedor.route) },
                        onNavigateToEdit = { id -> navController.navigate(Screen.EditProveedor().createRoute(id)) }
                    ) 
                }
                composable(Screen.MovimientoScreen.route) { 
                    MovimientoScreen(
                        viewModel = movimientoViewModel,
                        authViewModel = authViewModel,
                        onNavigateToCreate = { navController.navigate(Screen.CreateMovimiento.route) }
                    ) 
                }
                composable(Screen.OrdenCompraScreen.route) { 
                    OrdenCompraScreen(
                        viewModel = ordenViewModel,
                        authViewModel = authViewModel,
                        onNavigateToCreate = { navController.navigate(Screen.CreateOrdenCompra.route) }
                    ) 
                }
                
                // Creación y Edición
                composable(Screen.CreateProduct.route) { 
                    CreateProductScreen(
                        productoViewModel = productoViewModel,
                        categoriaViewModel = categoriaViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    ) 
                }
                composable(
                    route = Screen.EditProduct().route,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    CreateProductScreen(
                        productId = id,
                        productoViewModel = productoViewModel,
                        categoriaViewModel = categoriaViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.CreateCategory.route) {
                    CreateCategoryScreen(
                        viewModel = categoriaViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.EditCategory().route,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    CreateCategoryScreen(
                        categoriaId = id,
                        viewModel = categoriaViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.CreateProveedor.route) {
                    CreateProveedorScreen(
                        viewModel = proveedorViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.EditProveedor().route,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    CreateProveedorScreen(
                        proveedorId = id,
                        viewModel = proveedorViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.CreateOrdenCompra.route) {
                    CreateOrdenCompraScreen(
                        ordenViewModel = ordenViewModel,
                        proveedorViewModel = proveedorViewModel,
                        productoViewModel = productoViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.CreateMovimiento.route) {
                    CreateMovimientoScreen(
                        movimientoViewModel = movimientoViewModel,
                        productoViewModel = productoViewModel,
                        proveedorViewModel = proveedorViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.UserScreen.route) { UserScreen(authViewModel = authViewModel) }
            }
        }
    }
}

data class NavigationItem(val label: String, val route: String, val icon: ImageVector)

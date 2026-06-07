package com.ute.inventario.presentation.navigation

sealed class Screen(val route: String) {

    data object Login : Screen("login")
    data object Register : Screen("register")

    data object ProductoScreen : Screen("productos")
    data object CategoriaScreen : Screen("categorias")
    data object ProveedorScreen : Screen("proveedores")
    data object MovimientoScreen : Screen("movimientos")
    data object OrdenCompraScreen : Screen("ordenes_compra")

    data object UserScreen : Screen("perfil")
    
    // Rutas de Creación
    data object CreateProduct : Screen("create_product")
    data object CreateCategory : Screen("create_category")
    data object CreateProveedor : Screen("create_proveedor")
    data object CreateOrdenCompra : Screen("create_orden")
    data object CreateMovimiento : Screen("create_movimiento")

    // Rutas de Edición con Parámetros
    data class EditProduct(val id: Int = 0) : Screen("edit_product/{id}") {
        fun createRoute(id: Int) = "edit_product/$id"
    }
    
    data class EditCategory(val id: Int = 0) : Screen("edit_category/{id}") {
        fun createRoute(id: Int) = "edit_category/$id"
    }

    data class EditProveedor(val id: Int = 0) : Screen("edit_proveedor/{id}") {
        fun createRoute(id: Int) = "edit_proveedor/$id"
    }

    data class ProductDetail(val id: Int = 0) : Screen("producto/{id}") {
        fun createRoute(id: Int) = "producto/$id"
    }

    data class OrderDetail(val id: Int = 0) : Screen("orden_compra/{id}") {
        fun createRoute(id: Int) = "orden_compra/$id"
    }
}

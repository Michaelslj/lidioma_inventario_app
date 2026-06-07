package com.ute.inventario.presentation.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.OrdenCompraPayload
import com.ute.inventario.presentation.viewmodel.OrdenCompraViewModel
import com.ute.inventario.presentation.viewmodel.ProductoViewModel
import com.ute.inventario.presentation.viewmodel.ProveedorViewModel
import com.ute.inventario.presentation.viewmodel.OrdenCompraUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrdenCompraScreen(
    onNavigateBack: () -> Unit,
    ordenViewModel: OrdenCompraViewModel = hiltViewModel(),
    proveedorViewModel: ProveedorViewModel = hiltViewModel(),
    productoViewModel: ProductoViewModel = hiltViewModel()
) {
    val uiState by ordenViewModel.uiState.collectAsState()
    val proveedoresState by proveedorViewModel.uiState.collectAsState()
    val productosState by productoViewModel.uiState.collectAsState()

    var proveedorId by remember { mutableIntStateOf(0) }
    
    // Almacenamos ID del producto y su cantidad seleccionada
    val selectedItems = remember { mutableStateMapOf<Int, Int>() }
    
    var provExpanded by remember { mutableStateOf(false) }
    var prodExpanded by remember { mutableStateOf(false) }
    
    val isLoading = uiState is OrdenCompraUiState.Loading

    // Cálculo del Total Estimado
    val totalEstimado = remember(selectedItems.size) {
        var total = 0.0
        val allProds = (productosState as? com.ute.inventario.presentation.viewmodel.ProductoUiState.Success)?.productos ?: emptyList()
        selectedItems.forEach { (id, qty) ->
            val p = allProds.find { it.id == id }
            if (p != null) total += (p.precio * qty)
        }
        total
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Nueva Orden de Compra", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de Proveedor
            ExposedDropdownMenuBox(
                expanded = provExpanded,
                onExpandedChange = { if (!isLoading) provExpanded = !provExpanded }
            ) {
                val proveedores = (proveedoresState as? com.ute.inventario.presentation.viewmodel.ProveedorUiState.Success)?.proveedores ?: emptyList()
                val selectedProv = proveedores.find { it.id == proveedorId }
                OutlinedTextField(
                    value = selectedProv?.nombre ?: "Seleccionar Proveedor",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Proveedor") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(provExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(expanded = provExpanded, onDismissRequest = { provExpanded = false }) {
                    proveedores.forEach { prov ->
                        DropdownMenuItem(
                            text = { Text(prov.nombre) },
                            onClick = { proveedorId = prov.id; provExpanded = false }
                        )
                    }
                }
            }

            Text("Productos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            // Selector de Productos
            ExposedDropdownMenuBox(
                expanded = prodExpanded,
                onExpandedChange = { if (!isLoading) prodExpanded = !prodExpanded }
            ) {
                OutlinedTextField(
                    value = "Añadir Producto...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(prodExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(expanded = prodExpanded, onDismissRequest = { prodExpanded = false }) {
                    val productos = (productosState as? com.ute.inventario.presentation.viewmodel.ProductoUiState.Success)?.productos ?: emptyList()
                    productos.forEach { prod ->
                        val isSelected = selectedItems.containsKey(prod.id)
                        DropdownMenuItem(
                            text = { 
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = isSelected, onCheckedChange = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("${prod.nombre} ($${prod.precio})") 
                                }
                            },
                            onClick = {
                                if (isSelected) selectedItems.remove(prod.id)
                                else selectedItems[prod.id] = 1 // Cantidad por defecto
                            }
                        )
                    }
                }
            }

            // Lista de productos con CANTIDADES
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                selectedItems.forEach { (id, qty) ->
                    val productos = (productosState as? com.ute.inventario.presentation.viewmodel.ProductoUiState.Success)?.productos ?: emptyList()
                    val prod = productos.find { it.id == id }
                    if (prod != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(prod.nombre, fontWeight = FontWeight.Bold)
                                    Text("$${prod.precio} c/u", style = MaterialTheme.typography.bodySmall)
                                }
                                
                                OutlinedTextField(
                                    value = qty.toString(),
                                    onValueChange = { newValue ->
                                        val n = newValue.toIntOrNull() ?: 0
                                        if (n >= 0) selectedItems[id] = n
                                    },
                                    label = { Text("Cant.") },
                                    modifier = Modifier.width(70.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodySmall
                                )

                                IconButton(onClick = { selectedItems.remove(id) }) {
                                    Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            // Resumen de Total
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Estimado:", fontWeight = FontWeight.Bold)
                    Text("$${"%.2f".format(totalEstimado)}", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                }
            }

            if (uiState is OrdenCompraUiState.Error) {
                Text(
                    text = (uiState as OrdenCompraUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val generatedCode = "ORD-${System.currentTimeMillis().toString().takeLast(6)}"
                    ordenViewModel.crearOrden(
                        OrdenCompraPayload(
                            codigo_orden = generatedCode,
                            proveedor = proveedorId,
                            productos = selectedItems.keys.toList(),
                            estado = "PENDIENTE"
                        )
                    ) {
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = proveedorId != 0 && selectedItems.isNotEmpty() && !isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Emitir Orden de Compra", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

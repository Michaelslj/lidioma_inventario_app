package com.ute.inventario.presentation.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.MovimientoInventarioPayload
import com.ute.inventario.presentation.viewmodel.MovimientoInventarioViewModel
import com.ute.inventario.presentation.viewmodel.ProductoViewModel
import com.ute.inventario.presentation.viewmodel.ProveedorViewModel
import com.ute.inventario.presentation.viewmodel.MovimientoInventarioUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMovimientoScreen(
    onNavigateBack: () -> Unit,
    movimientoViewModel: MovimientoInventarioViewModel = hiltViewModel(),
    productoViewModel: ProductoViewModel = hiltViewModel(),
    proveedorViewModel: ProveedorViewModel = hiltViewModel()
) {
    val productosState by productoViewModel.uiState.collectAsState()
    val proveedoresState by proveedorViewModel.uiState.collectAsState()
    val movimientoUiState by movimientoViewModel.uiState.collectAsState()

    var productoId by remember { mutableIntStateOf(0) }
    var proveedorId by remember { mutableStateOf<Int?>(null) }
    var tipo by remember { mutableStateOf("ENTRADA") } 
    var cantidad by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    
    var prodExpanded by remember { mutableStateOf(false) }
    var provExpanded by remember { mutableStateOf(false) }
    
    val isLoading = movimientoUiState is MovimientoInventarioUiState.Loading

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Registrar Movimiento", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Tipo de Movimiento", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = tipo == "ENTRADA",
                    onClick = { tipo = "ENTRADA" },
                    label = { Text("Entrada (+)") },
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = tipo == "SALIDA",
                    onClick = { tipo = "SALIDA" },
                    label = { Text("Salida (-)") },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Selector de Producto
            ExposedDropdownMenuBox(
                expanded = prodExpanded,
                onExpandedChange = { if (!isLoading) prodExpanded = !prodExpanded }
            ) {
                val productos = (productosState as? com.ute.inventario.presentation.viewmodel.ProductoUiState.Success)?.productos ?: emptyList()
                val selectedProd = productos.find { it.id == productoId }
                OutlinedTextField(
                    value = selectedProd?.nombre ?: "Seleccionar Producto",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Producto") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(prodExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(expanded = prodExpanded, onDismissRequest = { prodExpanded = false }) {
                    productos.forEach { prod ->
                        DropdownMenuItem(
                            text = { Text("${prod.nombre} (Stock: ${prod.stock})") },
                            onClick = { productoId = prod.id; prodExpanded = false }
                        )
                    }
                }
            }

            if (tipo == "ENTRADA") {
                ExposedDropdownMenuBox(
                    expanded = provExpanded,
                    onExpandedChange = { if (!isLoading) provExpanded = !provExpanded }
                ) {
                    val proveedores = (proveedoresState as? com.ute.inventario.presentation.viewmodel.ProveedorUiState.Success)?.proveedores ?: emptyList()
                    val selectedProv = proveedores.find { it.id == proveedorId }
                    OutlinedTextField(
                        value = selectedProv?.nombre ?: "Proveedor (Opcional)",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Proveedor") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(provExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    )
                    ExposedDropdownMenu(expanded = provExpanded, onDismissRequest = { provExpanded = false }) {
                        DropdownMenuItem(text = { Text("Ninguno") }, onClick = { proveedorId = null; provExpanded = false })
                        proveedores.forEach { prov ->
                            DropdownMenuItem(
                                text = { Text(prov.nombre) },
                                onClick = { proveedorId = prov.id; provExpanded = false }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad a ${if(tipo == "ENTRADA") "Sumar" else "Restar"}") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo / Observaciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            if (movimientoUiState is MovimientoInventarioUiState.Error) {
                Text(
                    text = (movimientoUiState as MovimientoInventarioUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    movimientoViewModel.crearMovimiento(
                        MovimientoInventarioPayload(
                            producto = productoId,
                            proveedor = if (tipo == "ENTRADA") proveedorId else null,
                            tipo = tipo,
                            cantidad = cantidad.toIntOrNull() ?: 0,
                            motivo = motivo
                        )
                    ) {
                        // REFRESCAMOS LOS PRODUCTOS PARA QUE SE VEA EL CAMBIO DE STOCK AL INSTANTE
                        productoViewModel.obtenerProductos()
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = productoId != 0 && cantidad.isNotBlank() && motivo.isNotBlank() && !isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrar Movimiento", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

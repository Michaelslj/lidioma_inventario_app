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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.ProductoPayload
import com.ute.inventario.presentation.viewmodel.ProductoViewModel
import com.ute.inventario.presentation.viewmodel.CategoriaViewModel
import com.ute.inventario.presentation.viewmodel.ProductoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    productId: Int? = null,
    onNavigateBack: () -> Unit,
    productoViewModel: ProductoViewModel = hiltViewModel(),
    categoriaViewModel: CategoriaViewModel = hiltViewModel()
) {
    val uiState by productoViewModel.uiState.collectAsState()
    val categoriasState by categoriaViewModel.uiState.collectAsState()
    
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoriaId by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    val isEditing = productId != null

    // Cargar datos si es edición
    LaunchedEffect(productId) {
        if (isEditing && uiState is ProductoUiState.Success) {
            val producto = (uiState as ProductoUiState.Success).productos.find { it.id == productId }
            producto?.let {
                nombre = it.nombre
                descripcion = it.descripcion
                precio = it.precio.toString()
                stock = it.stock.toString()
                categoriaId = it.categoriaId ?: 0
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Producto" else "Nuevo Producto", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                val categorias = (categoriasState as? com.ute.inventario.presentation.viewmodel.CategoriaUiState.Success)?.categorias ?: emptyList()
                val selectedCat = categorias.find { it.id == categoriaId }
                
                OutlinedTextField(
                    value = selectedCat?.nombre ?: "Seleccionar Categoría",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.nombre) },
                            onClick = {
                                categoriaId = cat.id
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val payload = ProductoPayload(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        stock = stock.toIntOrNull() ?: 0,
                        categoriaId = categoriaId
                    )
                    if (isEditing) {
                        productoViewModel.actualizarProducto(productId!!, payload) { onNavigateBack() }
                    } else {
                        productoViewModel.crearProducto(payload) { onNavigateBack() }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = nombre.isNotBlank() && precio.isNotBlank() && stock.isNotBlank() && categoriaId != 0
            ) {
                Text(if (isEditing) "Actualizar Producto" else "Guardar Producto", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

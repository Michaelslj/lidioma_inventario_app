package com.ute.inventario.presentation.ui.proveedor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.Proveedor
import com.ute.inventario.presentation.components.ErrorScreen
import com.ute.inventario.presentation.components.LoadingScreen
import com.ute.inventario.presentation.viewmodel.AuthViewModel
import com.ute.inventario.presentation.viewmodel.ProveedorViewModel
import com.ute.inventario.presentation.viewmodel.ProveedorUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedorScreen(
    onNavigateToCreate: () -> Unit = {},
    onNavigateToEdit: (Int) -> Unit = {},
    viewModel: ProveedorViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    var proveedorToDelete by remember { mutableStateOf<Proveedor?>(null) }

    if (proveedorToDelete != null) {
        AlertDialog(
            onDismissRequest = { proveedorToDelete = null },
            title = { Text("Eliminar Proveedor") },
            text = { Text("¿Estás seguro de que deseas eliminar a '${proveedorToDelete?.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarProveedor(proveedorToDelete!!.id)
                        proveedorToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { proveedorToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Proveedores", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            if (currentUser?.isStaff == true) {
                FloatingActionButton(
                    onClick = onNavigateToCreate,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Add, "Agregar Proveedor")
                }
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is ProveedorUiState.Loading -> LoadingScreen("Cargando proveedores...")
            is ProveedorUiState.Error -> ErrorScreen(state.message) { viewModel.cargarProveedores() }
            is ProveedorUiState.Success -> {
                if (state.proveedores.isEmpty()) {
                    EmptyProveedorMessage()
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(padding).fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.proveedores) { proveedor ->
                            ProveedorCardPro(
                                proveedor = proveedor,
                                isStaff = currentUser?.isStaff == true,
                                onEdit = { onNavigateToEdit(proveedor.id) },
                                onDelete = { proveedorToDelete = proveedor }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProveedorCardPro(
    proveedor: Proveedor,
    isStaff: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Business, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(proveedor.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("RUC: ${proveedor.ruc}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(proveedor.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            
            if (isStaff) {
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyProveedorMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay proveedores registrados", color = Color.Gray)
    }
}

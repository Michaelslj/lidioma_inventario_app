package com.ute.inventario.presentation.ui.movimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.MovimientoInventario
import com.ute.inventario.presentation.components.ErrorScreen
import com.ute.inventario.presentation.components.LoadingScreen
import com.ute.inventario.presentation.viewmodel.AuthViewModel
import com.ute.inventario.presentation.viewmodel.MovimientoInventarioViewModel
import com.ute.inventario.presentation.viewmodel.MovimientoInventarioUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientoScreen(
    onNavigateToCreate: () -> Unit = {},
    viewModel: MovimientoInventarioViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    var movementToDelete by remember { mutableStateOf<MovimientoInventario?>(null) }

    if (movementToDelete != null) {
        AlertDialog(
            onDismissRequest = { movementToDelete = null },
            title = { Text("Eliminar Registro") },
            text = { Text("¿Deseas eliminar este registro de movimiento? Esto no revertirá el stock.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarMovimiento(movementToDelete!!.id)
                        movementToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { movementToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Historial de Movimientos", fontWeight = FontWeight.Bold) },
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
                    Icon(Icons.Default.Add, "Nuevo Movimiento")
                }
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is MovimientoInventarioUiState.Loading -> LoadingScreen("Cargando historial...")
            is MovimientoInventarioUiState.Error -> ErrorScreen(state.message) { viewModel.cargarMovimientos() }
            is MovimientoInventarioUiState.Success -> {
                if (state.movimientos.isEmpty()) {
                    EmptyMovimientoMessage()
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(padding).fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.movimientos) { movimiento ->
                            MovimientoCardPro(
                                movimiento = movimiento,
                                isStaff = currentUser?.isStaff == true,
                                onDelete = { movementToDelete = movimiento }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovimientoCardPro(
    movimiento: MovimientoInventario,
    isStaff: Boolean,
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
            val isEntry = movimiento.tipo == "E"
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background((if (isEntry) Color(0xFF38A169) else Color(0xFFE53E3E)).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isEntry) Icons.Default.SouthWest else Icons.Default.NorthEast,
                    contentDescription = null,
                    tint = if (isEntry) Color(0xFF38A169) else Color(0xFFE53E3E),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(movimiento.productoNombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("Por: ${movimiento.usuario}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(movimiento.creadoEn, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isEntry) "+" else "-"}${movimiento.cantidad}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isEntry) Color(0xFF38A169) else Color(0xFFE53E3E)
                )
                if (isStaff) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyMovimientoMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay movimientos registrados", color = Color.Gray)
    }
}

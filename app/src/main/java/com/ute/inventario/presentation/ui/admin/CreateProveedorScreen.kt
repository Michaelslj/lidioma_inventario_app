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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.ProveedorPayload
import com.ute.inventario.presentation.viewmodel.ProveedorViewModel
import com.ute.inventario.presentation.viewmodel.ProveedorUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProveedorScreen(
    proveedorId: Int? = null,
    onNavigateBack: () -> Unit,
    viewModel: ProveedorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var nombre by remember { mutableStateOf("") }
    var ruc by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    val isEditing = proveedorId != null
    val isLoading = uiState is ProveedorUiState.Loading

    LaunchedEffect(proveedorId) {
        if (isEditing && uiState is ProveedorUiState.Success) {
            val proveedor = (uiState as ProveedorUiState.Success).proveedores.find { it.id == proveedorId }
            proveedor?.let {
                nombre = it.nombre
                ruc = it.ruc
                telefono = it.telefono
                email = it.email
                direccion = it.direccion
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Proveedor" else "Nuevo Proveedor", fontWeight = FontWeight.Bold) },
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
                label = { Text("Nombre / Razón Social") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = ruc,
                onValueChange = { ruc = it },
                label = { Text("RUC / Identificación") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            if (uiState is ProveedorUiState.Error) {
                Text(
                    text = (uiState as ProveedorUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val payload = ProveedorPayload(nombre, ruc, telefono, email, direccion)
                    if (isEditing) {
                        viewModel.actualizarProveedor(proveedorId!!, payload) { onNavigateBack() }
                    } else {
                        viewModel.crearProveedor(payload) { onNavigateBack() }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = nombre.isNotBlank() && ruc.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (isEditing) "Actualizar" else "Guardar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

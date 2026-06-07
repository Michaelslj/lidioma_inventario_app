package com.ute.inventario.presentation.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.inventario.domain.model.CategoriaPayload
import com.ute.inventario.presentation.viewmodel.CategoriaViewModel
import com.ute.inventario.presentation.viewmodel.CategoriaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryScreen(
    categoriaId: Int? = null,
    onNavigateBack: () -> Unit,
    viewModel: CategoriaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    
    val isEditing = categoriaId != null
    val isLoading = uiState is CategoriaUiState.Loading

    LaunchedEffect(categoriaId) {
        if (isEditing && uiState is CategoriaUiState.Success) {
            val categoria = (uiState as CategoriaUiState.Success).categorias.find { it.id == categoriaId }
            categoria?.let {
                nombre = it.nombre
                descripcion = it.descripcion
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Categoría" else "Nueva Categoría", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la Categoría") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            )

            if (uiState is CategoriaUiState.Error) {
                Text(
                    text = (uiState as CategoriaUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val payload = CategoriaPayload(nombre, descripcion)
                    if (isEditing) {
                        viewModel.actualizarCategoria(categoriaId!!, payload) { onNavigateBack() }
                    } else {
                        viewModel.crearCategoria(payload) { onNavigateBack() }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = nombre.isNotBlank() && !isLoading,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (isEditing) "Actualizar" else "Guardar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

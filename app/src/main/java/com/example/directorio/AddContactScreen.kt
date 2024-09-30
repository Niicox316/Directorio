package com.example.directorio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddContactScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Agregar Contacto")
        // Aquí puedes agregar campos de texto para el nombre, teléfono, etc.

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Lógica para agregar el contacto
            navController.popBackStack() // Volver a la lista de contactos
        }) {
            Text(text = "Guardar Contacto")
        }
    }
}

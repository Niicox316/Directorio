package com.example.directorio

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("RememberReturnType")
@Composable
fun ContactListScreen(navController: NavController) {
    val contacts = remember { mutableStateListOf<Contact>() } // Lista mutable de contactos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lista de Contactos")

        // Aquí puedes agregar un LazyColumn para mostrar los contactos
        // Placeholder para contactos
        Text(text = "Contacto 1: Juan Pérez")
        Text(text = "Contacto 2: María López")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("addContact") }) {
            Text(text = "Agregar Contacto")
        }
    }
}


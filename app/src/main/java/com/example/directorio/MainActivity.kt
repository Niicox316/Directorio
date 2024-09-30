package com.example.directorio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController

// Universidad de Cundinamarca
// Línea de Profundización III
// Nicolás Guzmán

// Modelo de contacto
data class Contact(
    var name: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var address: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DirectoryApp() // Llama a la función principal de la app
        }
    }
}

// Componente principal de la aplicación
@Composable
fun DirectoryApp() {
    val navController = rememberNavController() // Controlador de navegación
    NavHost(navController, startDestination = "home") { // Navegación entre pantallas
        composable("home") { HomeScreen(navController) }
        composable("contact_detail/{contact}") { backStackEntry ->
            val contactString = backStackEntry.arguments?.getString("contact")
            ContactDetailScreen(contactString, navController) // Muestra los detalles del contacto
        }
    }
}

// Pantalla principal para agregar y ver contactos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var contacts by remember { mutableStateOf(mutableListOf<Contact>()) } // Lista de contactos
    var name by remember { mutableStateOf("") } // Nombre del contacto
    var lastName by remember { mutableStateOf("") } // Apellido del contacto
    var phone by remember { mutableStateOf("") } // Teléfono del contacto
    var email by remember { mutableStateOf("") } // Correo del contacto
    var address by remember { mutableStateOf("") } // Dirección del contacto
    var isEditing by remember { mutableStateOf(false) } // Estado de edición
    var editingContactIndex by remember { mutableStateOf(-1) } // Índice del contacto en edición

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Directorio Udec") }) // Título de la barra superior
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campos de texto para ingresar el nombre, apellido, teléfono, correo y dirección
                TextFieldWithLabel(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre"
                )

                TextFieldWithLabel(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Apellido"
                )

                TextFieldWithLabel(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Teléfono"
                )

                TextFieldWithLabel(
                    value = email,
                    onValueChange = { email = it },
                    label = "Correo"
                )

                TextFieldWithLabel(
                    value = address,
                    onValueChange = { address = it },
                    label = "Dirección"
                )

                // Botón para agregar o actualizar un contacto
                Button(onClick = {
                    if (isEditing) {
                        // Actualiza el contacto existente
                        contacts[editingContactIndex] = Contact(name, lastName, phone, email, address)
                        isEditing = false // Cambia el estado de edición
                    } else {
                        // Agrega un nuevo contacto
                        contacts.add(Contact(name, lastName, phone, email, address))
                    }
                    // Limpiar campos
                    name = ""
                    lastName = ""
                    phone = ""
                    email = ""
                    address = ""
                }) {
                    Text(if (isEditing) "Actualizar" else "Agregar Contacto") // Cambia el texto del botón según el estado
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de contactos
                LazyColumn {
                    items(contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            onEdit = {
                                // Inicia el modo de edición
                                val index = contacts.indexOf(contact)
                                name = contacts[index].name
                                lastName = contacts[index].lastName
                                phone = contacts[index].phone
                                email = contacts[index].email
                                address = contacts[index].address
                                editingContactIndex = index
                                isEditing = true
                            },
                            onDelete = {
                                // Eliminar el contacto de la lista
                                contacts.remove(contact)
                            },
                            onViewDetails = {
                                // Navega a la pantalla de detalles del contacto
                                navController.navigate("contact_detail/${contact.name}, ${contact.lastName}, ${contact.phone}, ${contact.email}, ${contact.address}")
                            }
                        )
                    }
                }
            }
        }
    )
}

// Componente para los campos de texto con etiquetas
@Composable
fun TextFieldWithLabel(value: String, onValueChange: (String) -> Unit, label: String) {
    var isFocused by remember { mutableStateOf(false) } // Estado de enfoque

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(BorderStroke(1.dp, Color.Gray))
            .padding(16.dp)
            .onFocusChanged { focusState -> isFocused = focusState.isFocused }, // Detecta si el campo está enfocado
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Muestra la etiqueta solo si el campo está vacío y no está enfocado
                if (value.isEmpty() && !isFocused) {
                    Text(label, color = Color.Gray)
                }
                innerTextField() // Muestra el campo de texto
            }
        }
    )
}

// Componente para mostrar cada contacto en la lista
@Composable
fun ContactItem(contact: Contact, onEdit: () -> Unit, onDelete: () -> Unit, onViewDetails: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(BorderStroke(1.dp, Color.LightGray))
            .padding(8.dp)
            .clickable { onViewDetails() }, // Navega a los detalles al hacer clic
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        // Muestra la información del contacto
        Text(text = "Nombre: ${contact.name}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Apellido: ${contact.lastName}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Teléfono: ${contact.phone}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Correo: ${contact.email}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Dirección: ${contact.address}", style = MaterialTheme.typography.bodyMedium)

        // Botón para eliminar el contacto
        Button(onClick = { onDelete() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Eliminar") // Cambia el texto del botón a "Eliminar"
        }
    }
}

// Pantalla de detalles del contacto
@Composable
fun ContactDetailScreen(contactString: String?, navController: NavController) {
    contactString?.let {
        val contactDetails = it.split(", ") // Separa los detalles del contacto
        val contact = Contact(contactDetails[0], contactDetails[1], contactDetails[2], contactDetails[3], contactDetails[4])

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Muestra los detalles del contacto
            Text(text = "Detalles del Contacto", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Nombre: ${contact.name}")
            Text(text = "Apellido: ${contact.lastName}")
            Text(text = "Teléfono: ${contact.phone}")
            Text(text = "Correo: ${contact.email}")
            Text(text = "Dirección: ${contact.address}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver") // Botón para volver a la pantalla anterior
            }
        }
    }
}

// Vista previa de la aplicación
@Preview(showBackground = true)
@Composable
fun PreviewDirectoryApp() {
    DirectoryApp()
}

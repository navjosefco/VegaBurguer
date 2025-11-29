package ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.formContrasenya

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun CambiarClaveForm(
    

    onClose: () -> Unit,
    onConfirm: (String, String) -> Unit, //oldPassword, newPassword

    cambiarClaveFormularioViewModel: CambiarClaveFormViewModel = viewModel {
        CambiarClaveFormViewModel()
    }

){

    val state by cambiarClaveFormularioViewModel.uiState.collectAsState()
    val isFormValid by cambiarClaveFormularioViewModel.isFormValid.collectAsState()

    //Variables de estado para la visibilidad de los campos de contrasenya
    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .defaultMinSize(minHeight = 200.dp),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ){
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Cambiar Contraseña",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Campos
            //Contrasenya actual
            OutlinedTextField(
                value = state.oldPassword,
                onValueChange = { cambiarClaveFormularioViewModel.onOldPasswordChange(it) },
                label = { Text("Contraseña Actual") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (oldPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                        Icon(if (oldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                    }
                },
                isError = state.oldPasswordError != null,
                supportingText = state.oldPasswordError?.let { { Text(it) } }
            )

            // Nueva Contrasenya
            OutlinedTextField(
                value = state.newPassword,
                onValueChange = { cambiarClaveFormularioViewModel.onNewPasswordChange(it) },
                label = { Text("Nueva Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null) },
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                    }
                },
                isError = state.newPasswordError != null,
                supportingText = state.newPasswordError?.let { { Text(it) } }
            )

            // Confirmar Contrasenya
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { cambiarClaveFormularioViewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.CheckCircleOutline, contentDescription = null) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                    }
                },
                isError = state.confirmPasswordError != null,
                supportingText = state.confirmPasswordError?.let { { Text(it) } }
            )

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            //Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Limpiar
                FilledTonalButton(onClick = { cambiarClaveFormularioViewModel.clear() }) {
                    Icon(Icons.Default.Autorenew, contentDescription = null)
                }

                // Botón Guardar
                Button(

                    onClick = {
                        cambiarClaveFormularioViewModel.submit(
                            onSuccess = { oldPass, newPass ->
                                onConfirm(oldPass, newPass)
                            },
                            onFailure = {}
                        )
                    },

                    enabled = isFormValid
                ) {

                    Icon(Icons.Default.Save, contentDescription = null)
                }

                FilledTonalButton(onClick = onClose) {

                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
fun CambiarClaveFormPreview() {
    MaterialTheme {
        CambiarClaveForm(
            onClose = {},
            onConfirm = { _, _ -> }
        )
    }
}
package ies.sequeros.com.dam.pmdm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.com.dam.pmdm.administrador.AdministradorViewModel
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.memoria.FileDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.memoria.MemDependienteRepository
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministrador
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministradorViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.DependientesViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel
import ies.sequeros.com.dam.pmdm.tpv.ui.TPVViewModel
import ies.sequeros.com.dam.pmdm.tpv.ui.escaparate.EscaparateTPV
import ies.sequeros.com.dam.pmdm.tpv.ui.escaparate.EscaparateViewModel
import ies.sequeros.com.dam.pmdm.tpv.ui.inicio.InicioTPV
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.login.Sesion
import ies.sequeros.com.dam.pmdm.administrador.ui.login.LoginViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.login.LoginScreen

@Suppress("ViewModelConstructorInComposable")
@Composable

fun App( dependienteRepositorio : IDependienteRepositorio,
         productoRepositorio: IProductoRepositorio,
         categoriaRepositorio: ICategoriaRepositorio,
         pedidoRepositorio: IPedidoRepositorio,
         almacenImagenes:AlmacenDatos) {

    //view model
    val appViewModel= viewModel {  AppViewModel() }
    val mainViewModel= remember { MainAdministradorViewModel() }
    val administradorViewModel= viewModel { AdministradorViewModel() }
    val dependientesViewModel = viewModel{ DependientesViewModel(
        dependienteRepositorio, almacenImagenes
    )}

    val productosViewModel = viewModel { ProductosViewModel(
        productoRepositorio, categoriaRepositorio, almacenImagenes)}

    val categoriasViewModel = viewModel {CategoriasViewModel(
            categoriaRepositorio,almacenImagenes)
    }

    val pedidosViewModel = viewModel {PedidosViewModel(
            pedidoRepositorio, productoRepositorio,almacenImagenes) }

    // VIEW MODELS TPV
    val tpvViewModel = viewModel { TPVViewModel(pedidoRepositorio) }
    val escaparateViewModel = viewModel { 
        EscaparateViewModel(categoriaRepositorio, productoRepositorio, almacenImagenes) 
    }

    // LOGIN & SESSION
    val loginViewModel = viewModel { LoginViewModel(dependienteRepositorio) }


    appViewModel.setWindowsAdatativeInfo( currentWindowAdaptiveInfo())
    val navController= rememberNavController()
//para cambiar el modo
    AppTheme(appViewModel.darkMode.collectAsState()) {

        NavHost(
            navController,
            startDestination = AppRoutes.Main
        ) {
            composable(AppRoutes.Main) {
                Principal({
                    navController.navigate(AppRoutes.Login) // Al Login
                },{
                    navController.navigate(AppRoutes.Dependiente) 
                },{
                    navController.navigate(AppRoutes.TPV_INICIO)
                })
            }
            // RUTA LOGIN
            composable(AppRoutes.Login) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {
                        navController.navigate(AppRoutes.Administrador) {
                            popUpTo(AppRoutes.Main)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable (AppRoutes.Administrador){
                if (Sesion.esAdmin()) {
                    MainAdministrador(appViewModel,mainViewModel,administradorViewModel,
                        dependientesViewModel, productosViewModel, categoriasViewModel, pedidosViewModel, {
                        Sesion.cerrar() // Cerrar sesión al salir
                        navController.popBackStack()
                    })
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate(AppRoutes.Login)
                    }
                }
            }

            // RUTA TPV: INICIO (Pide nombre)
            composable(AppRoutes.TPV_INICIO) {
                InicioTPV(
                    onComenzar = { nombreCliente ->
                        if(nombreCliente.isBlank()){
                            navController.popBackStack()
                        }else{
                            tpvViewModel.setCustomerName(nombreCliente)
                            navController.navigate(AppRoutes.TPV_ESCAPARATE)
                        }
                    }
                )
            }

            // RUTA TPV: ESCAPARATE (Catálogo y Carrito)
            composable(AppRoutes.TPV_ESCAPARATE) {
                EscaparateTPV(
                    tpvViewModel = tpvViewModel,
                    escaparateViewModel = escaparateViewModel,
                    onCancelarPedido = {
                        tpvViewModel.resetSession()
                        navController.popBackStack()
                    },
                    onVerCarrito = {
                       // Ahora confirmarPedido lanza su propia corrutina
                       tpvViewModel.confirmarPedido(onSuccess = {
                           navController.popBackStack()
                       })
                    }
                )
            }

        }
    }

}
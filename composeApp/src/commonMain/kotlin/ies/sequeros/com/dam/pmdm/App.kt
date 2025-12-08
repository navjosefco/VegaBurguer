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
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.BorrarDependienteUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.actualizar.ActualizarDependienteUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.crear.CrearDependienteUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.ListarDependientesUseCase
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
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.pedidos.registrar.RegistrarPedidoClienteUseCase
import ies.sequeros.com.dam.pmdm.tpv.aplicacion.productos.listar.ListarProductosPorCategoriaUseCase
import ies.sequeros.com.dam.pmdm.tpv.ui.TPVViewModel
import ies.sequeros.com.dam.pmdm.tpv.ui.escaparate.EscaparateTPV
import ies.sequeros.com.dam.pmdm.tpv.ui.escaparate.EscaparateViewModel
import ies.sequeros.com.dam.pmdm.tpv.ui.inicio.InicioTPV

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
    val productosViewModel = viewModel {
        ProductosViewModel(
            productoRepositorio,
            categoriaRepositorio,
            almacenImagenes
        )
    }
    val categoriasViewModel = viewModel {
        CategoriasViewModel(
            categoriaRepositorio,
            almacenImagenes
        )
    }
    val pedidosViewModel = viewModel {
        PedidosViewModel(
            pedidoRepositorio,
            productoRepositorio,
            almacenImagenes
        )
    }

    // USE CASES TPV
    val listarCategoriasUseCase = ListarCategoriasUseCase(categoriaRepositorio)
    val listarProductosUseCase = ListarProductosPorCategoriaUseCase(productoRepositorio)
    val registrarPedidoUseCase = RegistrarPedidoClienteUseCase(pedidoRepositorio)

    // VIEW MODELS TPV
    val tpvViewModel = viewModel { TPVViewModel(registrarPedidoUseCase) }
    val escaparateViewModel = viewModel { 
        EscaparateViewModel(listarCategoriasUseCase, listarProductosUseCase) 
    }


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
                    navController.navigate(AppRoutes.Administrador)
                },{
                    navController.navigate(AppRoutes.Dependiente) 
                },{
                    navController.navigate(AppRoutes.TPV_INICIO)
                })
            }
            composable (AppRoutes.Administrador){
                MainAdministrador(appViewModel,mainViewModel,administradorViewModel,
                    dependientesViewModel, productosViewModel, categoriasViewModel, pedidosViewModel, {
                    navController.popBackStack()
                })
            }

            // RUTA TPV: INICIO (Pide nombre)
            composable(AppRoutes.TPV_INICIO) {
                InicioTPV(
                    onComenzar = { nombreCliente ->
                        tpvViewModel.setCustomerName(nombreCliente)
                        navController.navigate(AppRoutes.TPV_ESCAPARATE)
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
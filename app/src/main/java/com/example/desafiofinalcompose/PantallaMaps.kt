package com.example.borrame.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.desafiofinalcompose.DatosCompartidos
import com.example.desafiofinalcompose.ViewModels.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.MapUiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlin.let
import kotlin.text.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMaps(navController: NavHostController) {

    val viewModel: MapViewModel = viewModel()
    val context = LocalContext.current

    val markers by viewModel.markers.collectAsState()
    val cameraPosition by viewModel.cameraPosition.collectAsState()
    val selectedCoordinates by viewModel.selectedCoordinates.collectAsState()

    val locationPermissionGranted = remember { mutableStateOf(false) }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val location = remember { mutableStateOf<Location?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = cameraPosition
    }

    val mapProperties = MapProperties(
        mapType = MapType.HYBRID,
        isMyLocationEnabled = locationPermissionGranted.value
    )

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            locationPermissionGranted.value = granted
        }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    (LaunchedEffect(locationPermissionGranted.value) {
        if (locationPermissionGranted.value) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                location.value = loc
            }
        }
    })

    LaunchedEffect(cameraPosition) {
        cameraPositionState.animate(
            CameraUpdateFactory.newCameraPosition(cameraPosition),
            400
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar ubicación") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF84F19),
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            //MAPA
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    tiltGesturesEnabled = true,
                    rotationGesturesEnabled = true
                ),
                onMapClick = { latLng ->
                    viewModel.selectCoordinates(latLng)
                    viewModel.updateCoordinates(latLng)
                }
            ) {

                selectedCoordinates?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Ubicación seleccionada"
                    )
                }

                location.value?.let {
                    Circle(
                        center = LatLng(it.latitude, it.longitude),
                        radius = 70.0,
                        strokeColor = Color.Blue,
                        fillColor = Color.Blue.copy(alpha = 0.4f)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = selectedCoordinates?.latitude?.toString().orEmpty(),
                    onValueChange = {},
                    label = { Text("Latitud") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = selectedCoordinates?.longitude?.toString().orEmpty(),
                    onValueChange = {},
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(1f)
                )
            }


            Button(
                onClick = {

                    if (selectedCoordinates == null) {
                        Toast.makeText(context, "Selecciona ubicación", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    DatosCompartidos.lat = selectedCoordinates!!.latitude
                    DatosCompartidos.lng = selectedCoordinates!!.longitude

                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF84F19),
                    contentColor = Color.Black
                )
            ) {
                Text("Confirmar ubicación")
            }
        }
    }
}
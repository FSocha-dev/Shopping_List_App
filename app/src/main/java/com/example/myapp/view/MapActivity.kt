package com.example.myapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapp.database.Store
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewModel.StoreViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel by viewModels<StoreViewModel>()
            val stores by myViewModel.stores.collectAsState()
            MyAppTheme {
                Surface {
                    MapActivityView(stores = stores, onBackButtonClick = {startShoppingListActivity() })
                }
            }
        }
    }
    private fun startShoppingListActivity(){
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun MapActivityView(stores: HashMap<String, Store>, onBackButtonClick: () -> Unit){
    Box (
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center)
    {
        Column(verticalArrangement = Arrangement.Center)
        {
            Button(
                onClick = onBackButtonClick
            ) {
                Text(text = "BACK", textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(16.dp))
            GoogleMapComposable(stores = stores)
        }
    }
}

@Composable
fun GoogleMapComposable(stores: HashMap<String, Store>) {
    val defaultPosition = LatLng(37.4, -122.08) // Pozycja domyślna, jeśli nie ma sklepów
    val firstStorePosition = stores.values.firstOrNull()?.let {
        LatLng(it.latitude.toDouble(), it.longitude.toDouble())
    } ?: defaultPosition

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(firstStorePosition, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp),
        cameraPositionState = cameraPositionState
    ) {
        stores.values.forEach { store ->
            val position = LatLng(store.latitude.toDouble(), store.longitude.toDouble())
            Marker(state = MarkerState(
                position = position),
                title = store.name,
                snippet = "Favourite: ${if (store.favourite) "Yes" else "No"}",
                icon = BitmapDescriptorFactory.defaultMarker(
                    if (store.favourite) BitmapDescriptorFactory.HUE_ORANGE
                    else BitmapDescriptorFactory.HUE_BLUE
                )
            )

        }
    }
}
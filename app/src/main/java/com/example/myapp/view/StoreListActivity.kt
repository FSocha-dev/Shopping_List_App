package com.example.myapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapp.database.Store
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewModel.StoreViewModel
import com.google.android.gms.location.LocationServices

class StoreListActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel by viewModels<StoreViewModel>()
            val stores by myViewModel.stores.collectAsState()
            MyAppTheme {
                Surface {
                    StoreListActivityView(stores = stores,
                        onAddStoreClick = { store -> myViewModel.addStore(store)},
                        onBackButtonClick = {startShoppingListActivity() },
                        onItemClick = { storeId -> startEditStoreActivity(storeId) },
                        onCheckedChange = { storeId, isChecked ->
                            stores[storeId]?.let { store ->
                                val updatedStore = store.copy(favourite = isChecked)
                                myViewModel.updateStore(updatedStore) }
                        },
                        getCurrentLocation = { store, onAddStoreClick ->
                            getCurrentLocation(store, onAddStoreClick)}
                    )

                }
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun startShoppingListActivity(){
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }

    fun startEditStoreActivity(storeId: String) {
        val intent = Intent(this, EditStoreActivity::class.java).apply {
            putExtra("id", storeId)
        }
        startActivity(intent)
    }
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(store: Store, onAddStoreClick: (Store) -> Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val updatedStore = store.copy(latitude = location.latitude.toString(), longitude = location.longitude.toString())
                    onAddStoreClick(updatedStore)
                } else {
                    Log.d("LocationError", "Location is null")
                }
            }
            .addOnFailureListener { e ->
                Log.d("LocationError", "Error getting location", e)
            }
    }

}

@Composable
fun StoreListActivityView(
    stores: HashMap<String, Store>,
    onAddStoreClick: (Store) -> Unit,
    onBackButtonClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onCheckedChange: (String, Boolean) -> Unit,
    getCurrentLocation: (Store, (Store) -> Unit) -> Unit)
{
    Column {
        Input(onAddStoreClick, onBackButtonClick, getCurrentLocation)
        StoreList(stores, onItemClick = onItemClick, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun Input(onAddStoreClick: (Store) -> Unit,
          onBackButtonClick: () -> Unit,
          getCurrentLocation: (Store, (Store) -> Unit) -> Unit) {
    val store = Store()
    val store_id by remember {  mutableStateOf("") }
    var storeName by remember {  mutableStateOf("") }
    var description by remember {  mutableStateOf("") }
    var radius by remember {  mutableStateOf("") }
    var favourite by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center)
    {
        Column (verticalArrangement = Arrangement.Center)
        {
            Button(onClick = onBackButtonClick
            ) {
                Text(text = "BACK", textAlign = TextAlign.Center)
            }
            Text(
                text = "STORE LIST",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(text = "Add store to the list:",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = storeName,
                onValueChange = { storeName = it },
                placeholder = { Text(text = "store name") }
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                placeholder = { Text(text = "description") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = radius,
                onValueChange = { radius = it },
                placeholder = { Text(text = "radius [m]") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                val newStore = Store(store_id, storeName, description, radius, "", "", favourite)
                getCurrentLocation(newStore, onAddStoreClick)
                storeName = ""
                description = ""
                radius = ""
                favourite = false
            }) {
                Text(text = "Add store",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreList(stores: HashMap<String, Store>,
              onItemClick: (String) -> Unit,
              onCheckedChange:(String, Boolean) -> Unit) {
    val storeList = stores.values.toList()
    Box {
        LazyColumn {
            items(
                items = storeList,
                key = { it.id }
            ) { store ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        onItemClick(store.id)

                    }
                ) {
                    Column {
                        Row {
                            Column {
                                Text(
                                    text = store.name,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                    )
                                Text(
                                    text = "radius: " + (store.radius) + "m",
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    textAlign = TextAlign.Center
                                    )
                                Text(
                                    text = "latitude: \n" + (store.latitude),
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "longitude: \n" + (store.longitude),
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.size(50.dp))
                            Column {
                                Text("Add to favourites")
                                Checkbox(
                                    checked = store.favourite,
                                    onCheckedChange = { isChecked ->
                                        onCheckedChange(store.id, isChecked) },
                                    colors = CheckboxDefaults.colors(Color.Green))
                                    }
                            }
                        Text(
                            text = "description: " + (store.description),
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            textAlign = TextAlign.Center)
                        }
                }
            }
        }
    }
}

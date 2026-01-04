package com.example.myapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.database.Store
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewModel.StoreViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

class EditStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel by viewModels<StoreViewModel>()
            val id = intent.getStringExtra("id") ?: ""
            val editStore = myViewModel.getStoreById(id)
            MyAppTheme {
                Surface {
                    EditStoreActivityView(
                        id = id,
                        editStore = editStore
                    ) {store ->
                        store.id = id
                        myViewModel.updateStore(store)
                        startStoreListActivity()
                    }
                }
            }
        }
    }

    private fun startStoreListActivity() {
        val intent = Intent(this, StoreListActivity::class.java)
        startActivity(intent)
    }
}
@Composable
fun EditStoreActivityView(
    id: String,
    editStore: Task<DataSnapshot>,
    onEditStoreClick: (Store) -> Unit
) {
    Column {
        EditStore(id, editStore, onEditStoreClick)
    }
}

@Composable
fun EditStore(
    id: String,
    editStore: Task<DataSnapshot>,
    onEditStoreClick: (Store) -> Unit
) {
    val store = editStore.result?.getValue(Store::class.java)

    var storeName by remember { mutableStateOf(store?.name.orEmpty()) }
    var description by remember { mutableStateOf(store?.description.orEmpty()) }
    var radius by remember { mutableStateOf(store?.radius.orEmpty()) }
    var latitude by remember { mutableStateOf(store?.latitude.orEmpty()) }
    var longitude by remember { mutableStateOf(store?.longitude.orEmpty()) }
    var favourite by remember { mutableStateOf(store?.favourite ?: false) }


    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EDIT STORE",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = " Edit store to the list:",
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
                placeholder = { Text(text = "description") }
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = radius,
                onValueChange = { radius = it },
                placeholder = { Text(text = "radius") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(onClick = {
                onEditStoreClick(Store(id, storeName, description, radius, latitude, longitude, favourite))
            }) {
                Text(
                    text = "Update store",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
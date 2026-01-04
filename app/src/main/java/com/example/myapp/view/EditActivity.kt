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
import com.example.myapp.database.ShoppingProduct
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewModel.ShoppingViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

class EditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel by viewModels<ShoppingViewModel>()
            val id = intent.getStringExtra("id") ?: ""
            val editProduct = myViewModel.getProductById(id)
            MyAppTheme {
                Surface {
                    EditActivityView(
                        id = id,
                        editProduct = editProduct
                    ) { shoppingProduct ->
                        shoppingProduct.id = id
                        myViewModel.updateProduct(shoppingProduct)
                        startShoppingListActivity()
                    }
                }
            }
        }
    }

    private fun startShoppingListActivity() {
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun EditActivityView(
    id: String,
    editProduct: Task<DataSnapshot>,
    onEditProductClick: (ShoppingProduct) -> Unit
) {
    Column {
        Edit(id, editProduct, onEditProductClick)
    }
}


@Composable
fun Edit(
    id: String,
    editProduct: Task<DataSnapshot>,
    onEditProductClick: (ShoppingProduct) -> Unit
) {
    val product = editProduct.result?.getValue(ShoppingProduct::class.java)

    var productName by remember { mutableStateOf(product?.name.orEmpty()) }
    var amount by remember { mutableStateOf(product?.amount.orEmpty()) }
    var price by remember { mutableStateOf(product?.price.orEmpty()) }

    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SHOPPING LIST APP",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = " Edit product to the list:",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = productName,
                onValueChange = { productName = it },
                placeholder = { Text(text = "product name") }
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text(text = "amount") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = price,
                onValueChange = { price = it },
                placeholder = { Text(text = "price") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(onClick = {
                onEditProductClick(ShoppingProduct(id, productName, amount, price))
            }) {
                Text(
                    text = "Update product",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

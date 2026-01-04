package com.example.myapp.view


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.database.ShoppingProduct
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewModel.ShoppingViewModel

class ShoppingListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel by viewModels<ShoppingViewModel>()
            val shoppingProducts by myViewModel.shoppingProducts.collectAsState()
            MyAppTheme {
                Surface {
                    ShoppingListActivityView(shoppingProducts = shoppingProducts,
                        onAddProductClick = { shoppingProduct -> myViewModel.addProduct(shoppingProduct)
                        },
                        onItemClick = { productId -> startEditActivity(productId) },
                        onMapButtonClick = { startMapActivity() },
                        onStoreListButtonClick = { startStoreListActivity() }
                    )
                }
            }
        }
    }

    fun startEditActivity(productId: String) {
        val intent = Intent(this, EditActivity::class.java).apply {
            putExtra("id", productId)
        }
        startActivity(intent)
    }

    private fun startMapActivity() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    private fun startStoreListActivity() {
        val intent = Intent(this, StoreListActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun ShoppingListActivityView(
    shoppingProducts: HashMap<String, ShoppingProduct>,
    onAddProductClick: (ShoppingProduct) -> Unit,
    onItemClick: (String) -> Unit,
    onMapButtonClick: () -> Unit,
    onStoreListButtonClick: () -> Unit)
{

    Column {
        Input(onAddProductClick, onMapButtonClick, onStoreListButtonClick)
        ShoppingList(shoppingProducts, onItemClick = onItemClick)
        }
    }


@Composable
fun Input(onAddProductClick: (ShoppingProduct) -> Unit, onMapButtonClick: () -> Unit, onStoreListButtonClick: () -> Unit) {
    val shoppingProduct = ShoppingProduct()
    val product_id by remember {  mutableStateOf("") }
    var productName by remember {  mutableStateOf("") }
    var amount by remember {  mutableStateOf("") }
    var price by remember {  mutableStateOf("") }

    Box (
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center)
    {
        Column (
            verticalArrangement = Arrangement.Center,
        ){
            Text(
                text = "SHOPPING LIST APP",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(text = "Add product to the list:",
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
                    keyboardType = KeyboardType.Number)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = price,
                onValueChange = { price = it },
                placeholder = { Text(text = "price") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                onAddProductClick(ShoppingProduct(product_id, productName, amount, price))
                productName = ""
                amount = ""
                price = "" })
            {
                Text(text =  "Add product",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onMapButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = "Map", textAlign = TextAlign.Center)
                }
                Button(
                    onClick = onStoreListButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = "Store list", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList(shoppingProducts: HashMap<String, ShoppingProduct>, onItemClick: (String) -> Unit) {
    val productList = shoppingProducts.values.toList()
    Box {
        LazyColumn {
            items(
                items = productList,
                key = { it.id }
            ) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        onItemClick(product.id)

                    }
                ) {
                    Row {
                        Text(
                            text = product.name,
                            modifier = Modifier
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "amount: " + (product.amount),
                            modifier = Modifier
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "price: " + (product.price) +"zł",
                            modifier = Modifier
                                .padding(10.dp),
                            textAlign = TextAlign.Center
                        ) }
                }
            }
        }
    }
}


package com.example.myapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.database.DatabaseProvider
import com.example.myapp.database.DatabaseProvider.repo
import com.example.myapp.database.ShoppingProduct
import com.example.myapp.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.launch


class ShoppingViewModel(repo: Repository = DatabaseProvider.repo) : ViewModel() {

    val shoppingProducts = repo.shoppingProducts

    fun addProduct(shoppingProduct: ShoppingProduct) {
        viewModelScope.launch {
            repo.insertShoppingProduct(shoppingProduct)
        }
    }

    fun updateProduct(shoppingProduct: ShoppingProduct) {
        viewModelScope.launch {
            repo.updateShoppingProduct(shoppingProduct)
        }
    }

    fun getProductById(id: String): Task<DataSnapshot> {
        return repo.getShoppingProduct(id)
    }

}

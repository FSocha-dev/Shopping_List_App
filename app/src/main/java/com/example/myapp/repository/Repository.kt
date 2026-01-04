package com.example.myapp.repository



import com.example.myapp.database.AppDatabase
import com.example.myapp.database.ShoppingProduct
import com.example.myapp.database.Store
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

class Repository(private val firestoreDatabase: AppDatabase)
{
    val shoppingProducts = firestoreDatabase.allShoppingProductMutable
    val stores = firestoreDatabase.allStoreMutable

    fun getShoppingProduct(id: String): Task<DataSnapshot> = firestoreDatabase.getShoppingProduct(id)

    fun insertShoppingProduct(shoppingProduct: ShoppingProduct){
        firestoreDatabase.insertShoppingProduct(shoppingProduct)
    }

    fun updateShoppingProduct(shoppingProduct: ShoppingProduct){
        firestoreDatabase.updateShoppingProduct(shoppingProduct)
    }

    fun getStore(id: String): Task<DataSnapshot> = firestoreDatabase.getStore(id)

    fun insertStore(store: Store){
        firestoreDatabase.insertStore(store)
    }

    fun updateStore(store: Store){
        firestoreDatabase.updateStore(store)
    }
}

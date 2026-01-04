package com.example.myapp.database

import android.util.Log
import com.example.myapp.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow


object DatabaseProvider {
    private val db = Firebase.database("https://myappdatabase-77ff2-default-rtdb.europe-west1.firebasedatabase.app/")
    val repo by lazy {
        Repository(
            firestoreDatabase = AppDatabase(db)
        )
    }

}

class AppDatabase(private val firebaseDatabase: FirebaseDatabase) {

    val allShoppingProductMutable = MutableStateFlow(HashMap<String, ShoppingProduct>())
    val allStoreMutable = MutableStateFlow(HashMap<String, Store>())

    private val Path1 = "users/shoppingProduct"
    private val Path2 = "users/Store"

    init
    {
        firebaseDatabase.getReference(Path1).addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val shoppingProduct = ShoppingProduct(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        amount = snapshot.child("amount").value as String,
                        price = snapshot.child("price").value as String,
                    )

                    allShoppingProductMutable.value = allShoppingProductMutable.value.toMutableMap().apply { put(shoppingProduct.id, shoppingProduct) } as HashMap<String, ShoppingProduct>
                    Log.i("shoppingProductAdd", "ShoppingProduct added: ${allShoppingProductMutable.value}")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val shoppingProduct = ShoppingProduct(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        amount = snapshot.child("amount").value as String,
                        price = snapshot.child("price").value as String,
                    )
                    allShoppingProductMutable.value = allShoppingProductMutable.value.toMutableMap().apply { put(shoppingProduct.id, shoppingProduct) } as HashMap<String, ShoppingProduct>
                    Log.i("shoppingProductChange", "ShoppingProduct updated: ${shoppingProduct.id}")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val shoppingProduct = ShoppingProduct(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        amount = snapshot.child("amount").value as String,
                        price = snapshot.child("price").value as String,
                    )

                    allShoppingProductMutable.value = allShoppingProductMutable.value.toMutableMap().apply { remove(shoppingProduct.id, shoppingProduct) } as HashMap<String, ShoppingProduct>
                    Log.i("removed", shoppingProduct.toString())
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { Log.i("ShoppingProduct List","Previous name: $previousChildName.") }

                override fun onCancelled(error: DatabaseError) { Log.e("DB error ShoppingProduct list", error.message) }
            }
        )

        firebaseDatabase.getReference(Path2).addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val store = Store(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        description = snapshot.child("description").value as String,
                        radius = snapshot.child("radius").value as String,
                        latitude = snapshot.child("latitude").value as String,
                        longitude = snapshot.child("longitude").value as String,
                        favourite = snapshot.child("favourite").getValue(Boolean::class.java) ?: false,
                    )

                    allStoreMutable.value = allStoreMutable.value.toMutableMap().apply { put(store.id, store) } as HashMap<String, Store>
                    Log.i("storeAdd", "Store added: ${allStoreMutable.value}")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val store = Store(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        description = snapshot.child("description").value as String,
                        radius = snapshot.child("radius").value as String,
                        latitude = snapshot.child("latitude").value as String,
                        longitude = snapshot.child("longitude").value as String,
                        favourite = snapshot.child("favourite").getValue(Boolean::class.java) ?: false,
                    )
                    allStoreMutable.value = allStoreMutable.value.toMutableMap().apply { put(store.id, store) } as HashMap<String, Store>
                    Log.i("storeChange", "Store updated: ${store.id}")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val store = Store(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        description = snapshot.child("description").value as String,
                        radius = snapshot.child("radius").value as String,
                        latitude = snapshot.child("latitude").value as String,
                        longitude = snapshot.child("longitude").value as String,
                        favourite = snapshot.child("favourite").getValue(Boolean::class.java) ?: false,
                    )

                    allStoreMutable.value = allStoreMutable.value.toMutableMap().apply { remove(store.id, store) } as HashMap<String, Store>
                    Log.i("removed", store.toString())
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { Log.i("Store List","Previous name: $previousChildName.") }

                override fun onCancelled(error: DatabaseError) { Log.e("DB error Store List", error.message) }
            }
        )
    }

    fun getShoppingProduct(id: String): Task<DataSnapshot> {
        return firebaseDatabase.getReference("$Path1/${id}").get()
    }

    fun insertShoppingProduct(shoppingProduct: ShoppingProduct){
        firebaseDatabase.getReference(Path1).push().also {
            shoppingProduct.id = it.ref.key!!
            it.setValue(shoppingProduct) }
        Log.i("shoppingProductInsert", "ShoppingProduct Inserted: ${shoppingProduct.id}")
    }

    fun updateShoppingProduct(shoppingProduct: ShoppingProduct){
        val ref = firebaseDatabase.getReference("$Path1/${shoppingProduct.id}")
        ref.child("name").setValue(shoppingProduct.name)
        ref.child("amount").setValue(shoppingProduct.amount)
        ref.child("price").setValue(shoppingProduct.price)
    }

    fun getStore(id: String): Task<DataSnapshot> {
        return firebaseDatabase.getReference("$Path2/${id}").get()
    }

    fun insertStore(store: Store){
        firebaseDatabase.getReference(Path2).push().also {
            store.id = it.ref.key!!
            it.setValue(store) }
        Log.i("storeInsert", "Store Inserted: ${store.id}")
    }

    fun updateStore(store: Store){
        val ref = firebaseDatabase.getReference("$Path2/${store.id}")
        ref.child("name").setValue(store.name)
        ref.child("description").setValue(store.description)
        ref.child("radius").setValue(store.radius)
        ref.child("latitude").setValue(store.latitude)
        ref.child("longitude").setValue(store.longitude)
        ref.child("favourite").setValue(store.favourite)
    }

}

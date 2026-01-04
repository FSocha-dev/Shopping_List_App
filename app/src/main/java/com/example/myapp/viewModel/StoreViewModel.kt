package com.example.myapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.database.DatabaseProvider
import com.example.myapp.database.DatabaseProvider.repo
import com.example.myapp.database.Store
import com.example.myapp.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.launch

class StoreViewModel(repo: Repository = DatabaseProvider.repo) : ViewModel() {

    val stores = repo.stores

    fun addStore(store: Store) {
        viewModelScope.launch {
            repo.insertStore(store)
        }
    }

    fun updateStore(store: Store) {
        viewModelScope.launch {
            repo.updateStore(store)
        }
    }

    fun getStoreById(id: String): Task<DataSnapshot> {
        return repo.getStore(id)
    }


}
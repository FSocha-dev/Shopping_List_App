package com.example.myapp.database

data class ShoppingProduct(
    var id: String,
    var name: String,
    var amount: String,
    var price: String,
)
{
    constructor() : this(
        id = "",
        name = "",
        amount = "",
        price = ""
    )
}




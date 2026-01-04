package com.example.myapp.database

data class Store(
    var id: String,
    var name: String,
    var description: String,
    var radius: String,
    var latitude: String,
    var longitude: String,
    var favourite: Boolean
)
{
    constructor() : this(
        id = "",
        name = "",
        description = "",
        radius = "",
        latitude = "",
        longitude = "",
        favourite = false
    )
}
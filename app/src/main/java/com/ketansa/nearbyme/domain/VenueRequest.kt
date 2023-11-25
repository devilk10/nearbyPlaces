package com.ketansa.nearbyme.domain


data class GetVenuesRequest(
    val perPage: Int = 10,
    val page: Int = 1,
    val lat: Double,
    val lon: Double,
    val range: String,
    val query: String?
)
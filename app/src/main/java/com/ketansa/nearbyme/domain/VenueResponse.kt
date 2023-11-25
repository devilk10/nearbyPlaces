package com.ketansa.nearbyme.domain

data class VenueResponse(
    val venues: List<Venue>
)

data class Venue(
    val name: String,
    val city: String,
    val url: String,
)

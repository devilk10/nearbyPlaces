package com.ketansa.nearbyme.data.api

import com.ketansa.nearbyme.domain.VenueResponse
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class VenueApiService(retrofit: Retrofit) {

    private val api: PlacesApi = retrofit.create(PlacesApi::class.java)


    suspend fun getVenues(
        perPage: Int,
        page: Int,
        clientId: String,
        lat: Double,
        lon: Double,
        range: String,
        query: String
    ): VenueResponse {
        return api.getVenues(perPage, page, clientId, lat, lon, range, query)
    }
}

interface PlacesApi {
    @GET("venues")
    suspend fun getVenues(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("client_id") clientId: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("range") range: String,
        @Query("q") query: String
    ): VenueResponse
}
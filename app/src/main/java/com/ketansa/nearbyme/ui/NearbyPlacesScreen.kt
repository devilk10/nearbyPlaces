package com.ketansa.nearbyme.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ketansa.nearbyme.domain.GetVenuesRequest
import com.ketansa.nearbyme.domain.Venue
import com.ketansa.nearbyme.ui.viewmodel.NearbyPlacesVM
import com.ketansa.nearbyme.ui.viewmodel.VenueState


@Composable
fun NearbyPlacesScreen(nearbyPlacesVM: NearbyPlacesVM) {
    LaunchedEffect(key1 = Unit) {
        nearbyPlacesVM.getVenues(
            request = GetVenuesRequest(
                page = 1,
                lat = 12.971599,
                lon = 77.594566,
                range = "12mi",
                query = ""
            )
        )
    }

    val venueState by nearbyPlacesVM.venueState.collectAsState()

    when (venueState) {
        is VenueState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is VenueState.Success -> {
            val venues = (venueState as VenueState.Success).response.venues
            PlaceList(venues)
        }

        is VenueState.Error -> {
            val errorMessage = (venueState as VenueState.Error).message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $errorMessage",
                )
            }
        }
    }

}

@Composable
fun PlaceList(places: List<Venue>) {
    LazyColumn {
        items(places.size) { place ->
            PlaceItem(place = places[place])
            Divider()
        }
    }
}

@Composable
fun PlaceItem(place: Venue) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(place.url),
            contentDescription = "Venue Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = place.name, fontWeight = FontWeight.Bold)
            Text(text = place.city, color = Color.Gray)
        }
    }
}


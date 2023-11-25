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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

val request = GetVenuesRequest(
    page = 1,
    lat = 12.971599,
    lon = 77.594566,
    range = "12mi",
    query = ""
)

@Composable
fun NearbyPlacesScreen(nearbyPlacesVM: NearbyPlacesVM) {
    var range by remember { mutableIntStateOf(12) }

    LaunchedEffect(key1 = Unit) {
        nearbyPlacesVM.getVenues(
            request = request
        )
    }

    val venueState by nearbyPlacesVM.venueState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (venueState) {
            is VenueState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is VenueState.Success -> {
                val places = (venueState as VenueState.Success).response.venues
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    LazyColumn {
                        items(places.size) { place ->
                            PlaceItem(place = places[place])
                            Divider()
                        }
                    }
                }
            }

            is VenueState.Error -> {
                val errorMessage = (venueState as VenueState.Error).message
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
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

        Slider(
            value = range.toFloat(),
            onValueChange = {
                range = it.toInt()
            },
            valueRange = 1f..100f,
            steps = 99,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp)
        )
    }

    DisposableEffect(range) {
        nearbyPlacesVM.getVenues(request = request.copy(range = "$range" + "mi"))
        onDispose { }
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


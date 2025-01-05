package com.example.weatherapp.data

import android.content.Context
import android.location.Geocoder
import com.example.weatherapp.R

sealed interface GeocodeResponse {
    data class Success(
        val coordinates: Pair<Double, Double>,
    ) : GeocodeResponse
    data class Error(val errorMessage: String) : GeocodeResponse
}

class GeocoderRepository(private val context: Context) {

    // Returns latitude and longitude from a city name
    // VERY IMPORTANT: Run on a background thread
    fun getCoordinatesFromCityName(locationName: String): GeocodeResponse {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if(addresses?.get(0) != null){
            val location = addresses[0]
            return GeocodeResponse.Success(Pair(location.latitude, location.longitude))
        }
        else{
            return GeocodeResponse.Error(context.getString(R.string.location_not_found))
        }
    }
}
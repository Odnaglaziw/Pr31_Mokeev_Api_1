package com.amok.apis

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)

data class Main(
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val description: String
)

data class Wind(
    val speed: Float
)
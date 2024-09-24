package com.amok.apis

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var cityEditText: EditText
    private lateinit var getWeatherButton: Button
    private lateinit var weatherTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cityEditText = findViewById(R.id.cityEditText)
        getWeatherButton = findViewById(R.id.getWeatherButton)
        weatherTextView = findViewById(R.id.weatherTextView)

        getWeatherButton.setOnClickListener {
            val city = cityEditText.text.toString().trim()
            if (city.isNotEmpty()) {
                getWeatherData(city)
            }else{
                Snackbar.make(it,"Введите название города", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun getWeatherData(cityName: String) {
        val apiKey = "18ac447af8e34c35c63d710a5c784f6e"
        val encodedCityName = URLEncoder.encode(cityName, "UTF-8")
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$encodedCityName&units=metric&lang=ru&appid=$apiKey"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val gson = Gson()
                val weatherResponse = gson.fromJson(response, WeatherResponse::class.java)
                displayWeatherData(weatherResponse)
            },
            { error ->
                val statusCode = error.networkResponse?.statusCode ?: -1
                if (statusCode == 404) {
                    weatherTextView.text = "Город не найден"
                } else {
                    Log.e("WeatherAPI", "Ошибка запроса: код $statusCode, сообщение: ${error.message}")
                    weatherTextView.text = "Ошибка получения данных"
                }
            })

        queue.add(stringRequest)
    }

    private fun displayWeatherData(weatherResponse: WeatherResponse) {
        val weatherInfo = """
            Город: ${weatherResponse.name}
            Температура: ${weatherResponse.main.temp}°C
            Ощущается как: ${weatherResponse.main.feels_like}°C
            Описание: ${weatherResponse.weather[0].description}
            Влажность: ${weatherResponse.main.humidity}%
            Давление: ${weatherResponse.main.pressure} гПа
            Скорость ветра: ${weatherResponse.wind.speed} м/с
        """.trimIndent()

        weatherTextView.text = weatherInfo
    }
}
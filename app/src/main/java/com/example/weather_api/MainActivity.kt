package com.example.weather_api

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_api.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("Patna")
        SearchCity()

    }

    private fun SearchCity() {
        val SearchView = binding.searchView
      SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
          override fun onQueryTextSubmit(query: String?): Boolean {
              if (query != null) {
                  fetchWeatherData(query)
              }
              return true
           }

          override fun onQueryTextChange(newText: String?): Boolean {
              return true
          }

      })
    }


    private fun fetchWeatherData(textview: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(textview,"34c8dd2523e1e5cb7596da08ea7dd2a3","metric")

        response.enqueue(object : Callback<weatherapp>{
            override fun onResponse(call: Call<weatherapp>, response: Response<weatherapp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition  = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxtemp = responseBody.main.temp_max
                    val mintemp = responseBody.main.temp_min

                    binding.temp.text= "$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp : $maxtemp °C"
                    binding.minTemp.text = "Min Temp : $mintemp °C"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windspeed m/s"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.sea.text = "$seaLevel hpa"
                    binding.condition.text = condition
                    binding.day.text = dayname(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.textView.text = "$textview"


                    Log.d("TAG", "onResponse: $temperature")

                    changeImage(condition)
                }
            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {
            }

        })

    }

    private fun changeImage(condition :String) {
        when (condition) {


            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds ","Clouds","Overcast","Mist","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Ligth Snow","Moderate Snow","Heavy Snow","Blizzard",->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }

    fun dayname(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

}





package com.pryma.hotelminibar

import com.pryma.hotelminibar.model.Checkout
import com.pryma.hotelminibar.model.Food
import com.pryma.hotelminibar.model.Room
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @GET("room")
    fun getRooms(): Call<List<Room>>

    @GET("food")
    fun getFoods(): Call<List<Food>>

    @Headers("Content-Type: application/json")
    @POST("checkout")
    fun checkout(@Body data: Checkout): Call<Checkout>

    companion object Factory {
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("https://f105-103-80-81-242.ap.ngrok.io/")
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}
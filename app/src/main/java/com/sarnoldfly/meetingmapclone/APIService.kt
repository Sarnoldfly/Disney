package com.sarnoldfly.meetingmapclone

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response       //response es un objeto en el que se inrtroducen

interface APIService {
    @GET("v2/directions/driving-car")   //para pedirle algo a una pagiina web, se usa el metodo GET



    suspend fun givemeroute(
        @Query("api_key") key:String,
        @Query("start", encoded = true)start:String,
        @Query("end", encoded = false)end:String
    ):Response<RouteResponse>
}



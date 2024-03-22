package com.sarnoldfly.meetingmapclone

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response       //response es un objeto en el que se inrtroducen

interface APIService {
    @GET("character")   //para pedirle algo a una pagiina web, se usa el metodo GET, a la url
    suspend fun giveMeCharacters():Response<CharactersData>
}
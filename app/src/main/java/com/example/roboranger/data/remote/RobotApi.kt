package com.example.roboranger.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface RobotApi {

    @GET("stop")
    suspend fun robotStop() : Response<Unit>
    @GET("back")
    suspend fun robotGoFront() : Response<Unit>

    @GET("go")
    suspend fun robotGoBack() : Response<Unit>

    @GET("left")
    suspend fun robotGoLeft() : Response<Unit>

    @GET("right")
    suspend fun robotGoRight() : Response<Unit>

    @GET("ledon")
    suspend fun turnOnLight() : Response<Unit>

    @GET("ledoff")
    suspend fun turnOffLight() : Response<Unit>


}
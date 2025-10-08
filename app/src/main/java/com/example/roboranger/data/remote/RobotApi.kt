package com.example.roboranger.data.remote

import retrofit2.http.GET

interface RobotApi {

    @GET("stop")
    suspend fun robotStop()
    @GET("back")
    suspend fun robotGoFront()

    @GET("go")
    suspend fun robotGoBack()

    @GET("left")
    suspend fun robotGoLeft()

    @GET("right")
    suspend fun robotGoRight()

    @GET("ledon")
    suspend fun turnOnLight()

    @GET("ledoff")
    suspend fun turnOffLight()


}
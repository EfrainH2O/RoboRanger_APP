package com.example.roboranger.interfaces

import retrofit2.http.GET

interface RobotApi {
    @GET("go")
    suspend fun robot_go_front() : Unit
}
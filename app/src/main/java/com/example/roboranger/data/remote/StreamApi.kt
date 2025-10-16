package com.example.roboranger.data.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming

interface StreamApi {

    @GET("stream")
    @Streaming
    fun cameraStream(): Call<ResponseBody>


}
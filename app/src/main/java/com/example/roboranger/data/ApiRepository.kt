package com.example.roboranger.data

import com.example.roboranger.data.local.TokenManager
import com.example.roboranger.data.remote.ApiService

class ApiRepository(
  private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

}
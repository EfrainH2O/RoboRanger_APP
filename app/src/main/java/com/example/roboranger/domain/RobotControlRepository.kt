package com.example.roboranger.domain

interface RobotControlRepository {
    suspend fun goFront()
    suspend fun goBack()
    suspend fun goLeft()
    suspend fun goRight()
    suspend fun stop()
    suspend fun setLight(on: Boolean)
}
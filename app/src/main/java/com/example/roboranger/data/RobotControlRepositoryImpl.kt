package com.example.roboranger.data

import com.example.roboranger.data.remote.RobotApi
import com.example.roboranger.domain.RobotControlRepository
import retrofit2.Response

class RobotControlRepositoryImpl(
    private val api: RobotApi
) : RobotControlRepository {

    override suspend fun goFront() = requireOk(api.robotGoFront())
    override suspend fun goBack()  = requireOk(api.robotGoBack())
    override suspend fun goLeft()  = requireOk(api.robotGoLeft())
    override suspend fun goRight() = requireOk(api.robotGoRight())
    override suspend fun stop()    = requireOk(api.robotStop())
    override suspend fun setLight(on: Boolean) {
        val resp: Response<Unit> = if (on) api.turnOnLight() else api.turnOffLight()
        requireOk(resp)
    }

    private fun requireOk(response: Response<*>) {
        if (!response.isSuccessful) {
            throw Exception("Robot control error: HTTP ${response.code()}")
        }
    }
}
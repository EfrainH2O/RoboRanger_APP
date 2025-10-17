package com.example.roboranger.data

import android.graphics.Bitmap
import com.example.roboranger.data.remote.StreamApi
import com.example.roboranger.domain.RobotStreamRepository
import com.example.roboranger.ui.views.control.MjpegInputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Call

class RobotStreamRepositoryImpl(
    private val api: StreamApi
) : RobotStreamRepository {

    override fun streamFrames(): Flow<Bitmap> = flow {
        val call: Call<ResponseBody> = api.cameraStream()
        val response = call.execute()
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Stream request failed: HTTP ${response.code()}")
        }

        val body = response.body()!!
        try {
            val input = body.byteStream()
            val mjpeg = MjpegInputStream(input)

            while (true) {
                val frame: Bitmap? = mjpeg.readMjpegFrame()
                if (frame == null) break
                emit(frame)
            }
        } finally {
            body.close()
        }
    }
}
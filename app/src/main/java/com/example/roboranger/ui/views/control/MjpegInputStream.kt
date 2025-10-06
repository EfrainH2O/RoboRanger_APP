// In your MjpegInputStream.kt file

package com.example.roboranger.util // Or your correct package

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream

class MjpegInputStream(inputStream: InputStream) : DataInputStream(BufferedInputStream(inputStream, FRAME_MAX_LENGTH)) {

    // Standard JPEG markers
    private val SOI_MARKER = byteArrayOf(0xFF.toByte(), 0xD8.toByte()) // Start of Image
    private val EOI_MARKER = byteArrayOf(0xFF.toByte(), 0xD9.toByte()) // End of Image
    private val CONTENT_LENGTH = "Content-Length"

    companion object {
        private const val FRAME_MAX_LENGTH = 200000 // Increased buffer size (e.g., 200KB)
    }


    /**
     * Reads a single MJPEG frame and returns it as a byte array.
     * This method contains multiple strategies to handle different stream formats.
     */
    @Throws(IOException::class)
    private fun readFrameData(): ByteArray? {
        // Read the stream line by line until we find the JPEG start marker.
        // This effectively skips the multipart boundary and headers.
        mark(FRAME_MAX_LENGTH)
        var headerLength = -1
        var foundSOI = false
        while (!foundSOI) {
            headerLength = findSequence(this, SOI_MARKER)
            if (headerLength != -1) {
                foundSOI = true
            } else {
                // If SOI not found, we've likely read a chunk of header data.
                // We can discard the buffer up to this point and keep reading.
                // This is a simple recovery for now. A more robust parser would read line-by-line.
            }
            // For simplicity, we assume the SOI is in the first big read.
            break
        }

        if (!foundSOI || headerLength == -1) {
            Log.d("ImageParser", "Could not find Start of Image marker in the initial buffer.")
            // This might happen if the buffer is too small or stream is weird.
            // Reading the whole available buffer to see what's inside.
            val availableBytes = this.available()
            if (availableBytes > 0) {
                val buffer = ByteArray(availableBytes)
                readFully(buffer)
                Log.d("ImageParser", "Buffer content: " + buffer.toString(Charsets.US_ASCII))
            }
            return null // No image found in this chunk
        }


        // We found the start of the image. Reset the stream to that point.
        reset()
        skipBytes(headerLength)

        // Now, the stream is positioned at the beginning of the JPEG data (FF D8).
        // Let's find the end of the image.
        mark(FRAME_MAX_LENGTH)
        val eoiIndex = findSequence(this, EOI_MARKER)

        if (eoiIndex != -1) {
            // Found a complete frame in our buffer!
            reset() // Rewind to the SOI marker position

            // Read the complete image data from SOI to EOI
            val imageSize = eoiIndex + EOI_MARKER.size
            val imageData = ByteArray(imageSize)
            readFully(imageData)

            Log.d("ImageParser", "Successfully extracted frame!")
            return imageData
        }

        Log.e("ImageParser", "Image start found, but end is larger than buffer. Increase FRAME_MAX_LENGTH if this repeats.")
        // If we found a start but no end, the frame might be larger than our buffer.
        return null
    }

    // A helper function to find a sequence in a DataInputStream directly
    @Throws(IOException::class)
    private fun findSequence(ins: DataInputStream, sequence: ByteArray): Int {
        var matchIndex = 0
        val buffer = ByteArray(1024) // Read in 1KB chunks
        var totalBytesRead = 0

        while (true) {
            val bytesRead = ins.read(buffer)
            if (bytesRead == -1) return -1 // End of stream

            for (i in 0 until bytesRead) {
                if (buffer[i] == sequence[matchIndex]) {
                    matchIndex++
                    if (matchIndex == sequence.size) {
                        // Found the full sequence
                        return totalBytesRead + i - (sequence.size - 1)
                    }
                } else {
                    matchIndex = 0
                }
            }
            totalBytesRead += bytesRead
        }
    }

    /**
     * Public method to read the next frame and decode it into a Bitmap.
     */
    @Throws(IOException::class)
    fun readMjpegFrame(): Bitmap? {
        return try {
            val frameData = readFrameData()
            if (frameData != null) {
                BitmapFactory.decodeStream(ByteArrayInputStream(frameData))
            } else {
                null
            }
        } catch (e: IOException) {
            Log.e("ImageParser", e.message ?: "Na")
            e.printStackTrace()
            null
        }
    }
}

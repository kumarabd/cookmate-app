package com.example.myapplication.ui.home

import com.example.myapplication.BuildConfig
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer

class ModelAPIClient {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val baseUrl = BuildConfig.BASE_URL

    var interpreter: Interpreter? = null

    fun getModel(onSuccess: () -> Unit, onFailure: (message: String) -> Unit) {
        val url = "$baseUrl/scan/model"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                val modelData = ByteBuffer.wrap(response.body?.bytes())
                if (response.isSuccessful && modelData != null) {
                    try {
                        // Load the Tensorflow Lite model from the model data
                        val interpreter = Interpreter(modelData)
                        this@ModelAPIClient.interpreter = interpreter
                        onSuccess()
                    } catch (e: Exception) {
                        onFailure("Failed to load model: ${e.message}")
                    }
                } else {
                    onFailure("Failed to retrieve model: ${response.code} - ${response.message}")
                }
            }
        })
    }

    fun postImage(imageData: ByteArray, onSuccess: (result: String) -> Unit, onFailure: (message: String) -> Unit) {
        val url = "$baseUrl/scan/image"
        val mediaType = "image/jpeg".toMediaType()
        val body = imageData.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                if (response.isSuccessful && !result.isNullOrEmpty()) {
                    onSuccess(result)
                } else {
                    onFailure("Failed to process image: ${response.code} - ${response.message}")
                }
            }
        })
    }
}

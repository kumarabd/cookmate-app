package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HomeViewModel : ViewModel() {
    private var interpreter: Interpreter? = null
    private val modelAPIClient = ModelAPIClient()

    fun loadModel(onSuccess: () -> Unit, onFailure: (message: String) -> Unit) {
        modelAPIClient.getModel(
            onSuccess = {
                interpreter = modelAPIClient.interpreter
                onSuccess()
            },
            onFailure = onFailure
        )
    }

    fun processImage(imageData: ByteBuffer, onSuccess: (result: String) -> Unit, onFailure: (message: String) -> Unit) {
        if (interpreter == null) {
            onFailure("Model not loaded")
            return
        }

        // Define the input and output buffers for the model
        val inputBuffer = ByteBuffer.allocateDirect(IMAGE_SIZE * IMAGE_SIZE * PIXEL_SIZE * BATCH_SIZE)
        val outputBuffer = ByteBuffer.allocateDirect(OUTPUT_SIZE * FLOAT_SIZE * BATCH_SIZE)
        inputBuffer.order(ByteOrder.nativeOrder())
        outputBuffer.order(ByteOrder.nativeOrder())

        // Process the image with the model
        try {
            interpreter?.run(imageData, outputBuffer)
            val result = outputBuffer.getFloat(0)
            onSuccess(result.toString())
        } catch (e: Exception) {
            onFailure("Failed to process image: ${e.message}")
        }
    }

    companion object {
        private const val IMAGE_SIZE = 224
        private const val PIXEL_SIZE = 3
        private const val BATCH_SIZE = 1
        private const val FLOAT_SIZE = 4
        private const val OUTPUT_SIZE = 1
    }
}
package com.example.myapplication.ui.home

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class CameraPreview(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var mHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
    }

    init {
        // Check for camera permission at runtime on API level 23 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Request camera permission if not granted
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = holder
        mHolder?.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        unReleaseCamera()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Stop the preview and release the camera resources
        releaseCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder?.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera?.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera?.setPreviewDisplay(mHolder)
            mCamera?.startPreview()
        } catch (e: Exception) {
            // Log any exceptions that occur to the console
            e.printStackTrace()
        }
    }

    fun setCameraToCapture(callback: Camera.PictureCallback) {
        val parameters = mCamera?.parameters
        parameters?.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        mCamera?.parameters = parameters

        mCamera?.autoFocus { success, camera ->
            if (success) {
                camera.takePicture(null, null, callback)
            } else {
                Log.e("CameraPreview", "Failed to autofocus")
            }
        }

        // Capture the image
        try {
            mCamera?.takePicture(null, null, callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun releaseCamera() {
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

    fun unReleaseCamera() {
        // The Surface has been created, now tell the camera where to draw the preview.
        // Open the camera if it's not already open
        if (mCamera == null) {
            try {
                mCamera = Camera.open()
            } catch (e: Exception) {
                // Camera is not available (in use or does not exist)
                // Log any exceptions that occur to the console
                e.printStackTrace()
            }
        }

        // Set the preview display
        mCamera?.setDisplayOrientation(90)
        try {
            mCamera?.setPreviewDisplay(holder)
            mCamera?.startPreview()
        } catch (e: Exception) {
            // Log any exceptions that occur to the console
            e.printStackTrace()
        }
    }
}